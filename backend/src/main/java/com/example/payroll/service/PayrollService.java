
// ============================================================
// FILE: src/main/java/com/example/payroll/service/PayrollService.java
// ============================================================
package com.example.payroll.service;

import com.example.payroll.entity.*;
import com.example.payroll.enums.PayrollStatus;
import com.example.payroll.exception.ResourceNotFoundException;
import com.example.payroll.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final PayrollRunRepository payrollRunRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryStructureRepository salaryStructureRepository;
    private final AttendanceRepository attendanceRepository;
    private final AllowanceRepository allowanceRepository;
    private final DeductionRepository deductionRepository;

    // PF Constants
    private static final BigDecimal PF_EMPLOYEE_RATE = new BigDecimal("0.12");
    private static final BigDecimal PF_EMPLOYER_RATE = new BigDecimal("0.13");
    private static final BigDecimal PF_WAGE_CEILING = new BigDecimal("15000");

    // ESI Constants
    private static final BigDecimal ESI_EMPLOYEE_RATE = new BigDecimal("0.0075");
    private static final BigDecimal ESI_EMPLOYER_RATE = new BigDecimal("0.0325");
    private static final BigDecimal ESI_WAGE_CEILING = new BigDecimal("21000");

    @Transactional
    public PayrollRun initiatePayrollRun(Integer month, Integer year, Integer initiatedBy) {
        if (payrollRunRepository.existsByMonthAndYear(month, year)) {
            throw new RuntimeException("Payroll run already exists for " + month + "/" + year);
        }

        PayrollRun run = PayrollRun.builder()
                .month(month)
                .year(year)
                .status(PayrollStatus.DRAFT)
                .initiatedBy(initiatedBy)
                .build();

        return payrollRunRepository.save(run);
    }

    @Transactional
    public PayrollRun computePayroll(Integer runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));

        if (run.getStatus() == PayrollStatus.APPROVED || run.getStatus() == PayrollStatus.DISBURSED) {
            throw new RuntimeException("Payroll is already approved/disbursed for this period");
        }

        List<Employee> activeEmployees = employeeRepository.findByStatus("ACTIVE");
        List<Payroll> payrollRecords = new ArrayList<>();
        BigDecimal totalGross = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;

        for (Employee employee : activeEmployees) {
            Payroll payroll = computeEmployeePayroll(employee, run);
            payrollRecords.add(payroll);
            totalGross = totalGross.add(payroll.getGrossSalary());
            totalNet = totalNet.add(payroll.getNetSalary());
        }

        payrollRepository.saveAll(payrollRecords);

        run.setStatus(PayrollStatus.COMPUTED);
        run.setTotalGross(totalGross);
        run.setTotalNet(totalNet);
        run.setEmployeeCount(activeEmployees.size());

        return payrollRunRepository.save(run);
    }

    private Payroll computeEmployeePayroll(Employee employee, PayrollRun run) {
        // Get salary structure
        SalaryStructure structure = salaryStructureRepository
                .findByEmployeeEmployeeIdAndIsActiveTrue(employee.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salary structure not found for employee: " + employee.getEmployeeId()));

        // Get attendance (LOP days)
        BigDecimal lopDays = BigDecimal.ZERO;
        List<Attendance> attendanceList = attendanceRepository
                .findByEmployeeEmployeeIdAndMonthAndYear(employee.getEmployeeId(), run.getMonth(), run.getYear());
        if (!attendanceList.isEmpty()) {
            lopDays = attendanceList.get(0).getLopDays() != null ? attendanceList.get(0).getLopDays() : BigDecimal.ZERO;
        }

        // Calculate working days (assume 30 days per month)
        BigDecimal totalDays = new BigDecimal("30");
        BigDecimal workedDays = totalDays.subtract(lopDays);
        BigDecimal dayFactor = workedDays.divide(totalDays, 4, RoundingMode.HALF_UP);

        // Calculate earnings
        BigDecimal basicEarned = structure.getBasic().multiply(dayFactor).setScale(2, RoundingMode.HALF_UP);
        BigDecimal hraEarned = basicEarned.multiply(structure.getHraPct().divide(new BigDecimal("100")))
                .setScale(2, RoundingMode.HALF_UP);

        // Other earnings (DA + Special Allowance + Medical)
        BigDecimal otherEarnings = structure.getDa()
                .add(structure.getSpecialAllowance())
                .add(structure.getMedicalAllowance())
                .multiply(dayFactor)
                .setScale(2, RoundingMode.HALF_UP);

        // Additional allowances from allowance table
        List<Allowance> allowances = allowanceRepository
                .findByEmployeeEmployeeIdAndIsActiveTrue(employee.getEmployeeId());
        for (Allowance allowance : allowances) {
            otherEarnings = otherEarnings.add(allowance.getAmount());
        }

        BigDecimal grossSalary = basicEarned.add(hraEarned).add(otherEarnings);

        // 1. PF Calculation (12% employee on basic, capped at 15000)
        BigDecimal pfWage = basicEarned.min(PF_WAGE_CEILING);
        BigDecimal pfEmployee = pfWage.multiply(PF_EMPLOYEE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal pfEmployer = pfWage.multiply(PF_EMPLOYER_RATE).setScale(2, RoundingMode.HALF_UP);

        // 2. ESI Calculation (if gross <= 21000)
        BigDecimal esiEmployee = BigDecimal.ZERO;
        BigDecimal esiEmployer = BigDecimal.ZERO;
        if (grossSalary.compareTo(ESI_WAGE_CEILING) <= 0) {
            esiEmployee = grossSalary.multiply(ESI_EMPLOYEE_RATE).setScale(2, RoundingMode.HALF_UP);
            esiEmployer = grossSalary.multiply(ESI_EMPLOYER_RATE).setScale(2, RoundingMode.HALF_UP);
        }

        // 3. Professional Tax (simplified - Karnataka slab)
        BigDecimal professionalTax = calculateProfessionalTax(grossSalary);

        // 4. TDS
        BigDecimal tds = calculateTDS(grossSalary, pfEmployee);

        // Additional deductions from deduction table
        BigDecimal otherDeductions = BigDecimal.ZERO;
        List<Deduction> deductions = deductionRepository
                .findByEmployeeEmployeeIdAndIsActiveTrue(employee.getEmployeeId());
        for (Deduction deduction : deductions) {
            otherDeductions = otherDeductions.add(deduction.getAmount());
        }

        // Total deductions
        BigDecimal totalDeductions = pfEmployee.add(esiEmployee).add(professionalTax).add(tds).add(otherDeductions);

        // Net salary
        BigDecimal netSalary = grossSalary.subtract(totalDeductions);

        // Build payroll record
        return Payroll.builder()
                .employee(employee)
                .payrollRun(run)
                .payrollMonth(run.getMonth() + "/" + run.getYear())
                .basicEarned(basicEarned)
                .hraEarned(hraEarned)
                .otherEarnings(otherEarnings)
                .grossSalary(grossSalary)
                .pfEmployee(pfEmployee)
                .pfEmployer(pfEmployer)
                .esiEmployee(esiEmployee)
                .esiEmployer(esiEmployer)
                .professionalTax(professionalTax)
                .tds(tds)
                .otherDeductions(otherDeductions)
                .totalDeductions(totalDeductions)
                .netSalary(netSalary)
                .lopDays(lopDays)
                .paymentStatus("COMPUTED")
                .build();
    }

    private BigDecimal calculateProfessionalTax(BigDecimal grossSalary) {
        if (grossSalary.compareTo(new BigDecimal("15000")) <= 0) {
            return BigDecimal.ZERO;
        } else if (grossSalary.compareTo(new BigDecimal("25000")) <= 0) {
            return new BigDecimal("150");
        } else {
            return new BigDecimal("200");
        }
    }

    private BigDecimal calculateTDS(BigDecimal grossSalary, BigDecimal pfEmployee) {
        BigDecimal annualGross = grossSalary.multiply(new BigDecimal("12"));
        BigDecimal standardDeduction = new BigDecimal("50000");
        BigDecimal annualPF = pfEmployee.multiply(new BigDecimal("12"));
        BigDecimal taxableIncome = annualGross.subtract(standardDeduction).subtract(annualPF);

        BigDecimal annualTax = BigDecimal.ZERO;
        if (taxableIncome.compareTo(new BigDecimal("300000")) <= 0) {
            annualTax = BigDecimal.ZERO;
        } else if (taxableIncome.compareTo(new BigDecimal("700000")) <= 0) {
            annualTax = taxableIncome.subtract(new BigDecimal("300000")).multiply(new BigDecimal("0.05"));
        } else if (taxableIncome.compareTo(new BigDecimal("1000000")) <= 0) {
            annualTax = new BigDecimal("20000")
                    .add(taxableIncome.subtract(new BigDecimal("700000")).multiply(new BigDecimal("0.10")));
        } else if (taxableIncome.compareTo(new BigDecimal("1200000")) <= 0) {
            annualTax = new BigDecimal("50000")
                    .add(taxableIncome.subtract(new BigDecimal("1000000")).multiply(new BigDecimal("0.15")));
        } else if (taxableIncome.compareTo(new BigDecimal("1500000")) <= 0) {
            annualTax = new BigDecimal("80000")
                    .add(taxableIncome.subtract(new BigDecimal("1200000")).multiply(new BigDecimal("0.20")));
        } else {
            annualTax = new BigDecimal("140000")
                    .add(taxableIncome.subtract(new BigDecimal("1500000")).multiply(new BigDecimal("0.30")));
        }

        return annualTax.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
    }

    @Transactional
    public PayrollRun approvePayroll(Integer runId, Integer approvedBy) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));

        if (run.getStatus() != PayrollStatus.COMPUTED) {
            throw new RuntimeException("Payroll must be in COMPUTED status to approve");
        }

        run.setStatus(PayrollStatus.APPROVED);
        run.setApprovedBy(approvedBy);
        run.setApprovedAt(LocalDateTime.now());
        return payrollRunRepository.save(run);
    }

    @Transactional
    public PayrollRun disbursePayroll(Integer runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));

        if (run.getStatus() != PayrollStatus.APPROVED) {
            throw new RuntimeException("Payroll must be APPROVED before disbursement");
        }

        List<Payroll> payrolls = payrollRepository.findByPayrollRunRunId(runId);
        for (Payroll payroll : payrolls) {
            payroll.setPaymentStatus("PAID");
        }
        payrollRepository.saveAll(payrolls);

        run.setStatus(PayrollStatus.DISBURSED);
        run.setDisbursedAt(LocalDateTime.now());
        return payrollRunRepository.save(run);
    }

    public List<Payroll> getPayrollByEmployee(Integer employeeId) {
        return payrollRepository.findByEmployeeEmployeeId(employeeId);
    }

    public List<Payroll> getPayrollByRun(Integer runId) {
        return payrollRepository.findByPayrollRunRunId(runId);
    }

    public PayrollRun getPayrollRun(Integer runId) {
        return payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));
    }

    public List<PayrollRun> getAllPayrollRuns() {
        return payrollRunRepository.findAll();
    }
}
