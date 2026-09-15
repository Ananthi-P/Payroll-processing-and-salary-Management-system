// ============================================================
// FILE: src/main/java/com/example/payroll/service/PayslipService.java
// ============================================================
package com.example.payroll.service;
import com.example.payroll.entity.Payroll;
import com.example.payroll.entity.Payslip;
import com.example.payroll.exception.ResourceNotFoundException;
import com.example.payroll.repository.PayrollRepository;
import com.example.payroll.repository.PayslipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PayslipService {
    private final PayslipRepository payslipRepository;
    private final PayrollRepository payrollRepository;
    public Payslip generatePayslip(Integer payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found: " + payrollId));
        // Check if payslip already exists
        if (payslipRepository.findByPayrollPayrollId(payrollId).isPresent()) {
            throw new RuntimeException("Payslip already generated for payroll: " + payrollId);
        }
        String filePath = "/payslips/" + payroll.getEmployee().getEmployeeId()
                + "_" + payroll.getPayrollMonth().replace("/", "_") + ".pdf";
        Payslip payslip = Payslip.builder()
                .payroll(payroll)
                .generatedDate(LocalDate.now())
                .filePath(filePath)
                .isEmailed(false)
                .build();
        return payslipRepository.save(payslip);
    }
    public void generateBulkPayslips(Integer runId) {
        List<Payroll> payrolls = payrollRepository.findByPayrollRunRunId(runId);
        for (Payroll payroll : payrolls) {
            if (payslipRepository.findByPayrollPayrollId(payroll.getPayrollId()).isEmpty()) {
                generatePayslip(payroll.getPayrollId());
            }
        }
    }
    public Payslip getPayslipByPayrollId(Integer payrollId) {
        return payslipRepository.findByPayrollPayrollId(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found for payroll: " + payrollId));
    }
    public Payslip markAsEmailed(Integer payslipId) {
        Payslip payslip = payslipRepository.findById(payslipId)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found: " + payslipId));
        payslip.setIsEmailed(true);
        payslip.setEmailedAt(java.time.LocalDateTime.now());
        return payslipRepository.save(payslip);
    }
}

