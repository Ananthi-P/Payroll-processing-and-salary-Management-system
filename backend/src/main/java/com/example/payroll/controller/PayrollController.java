// ============================================================
// FILE: src/main/java/com/example/payroll/controller/PayrollController.java
// ============================================================
package com.example.payroll.controller;

import com.example.payroll.entity.Payroll;
import com.example.payroll.entity.PayrollRun;
import com.example.payroll.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payroll-runs")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;
    @GetMapping
    public ResponseEntity<List<PayrollRun>> getAllPayrollRuns() {
        return ResponseEntity.ok(payrollService.getAllPayrollRuns());
    }
    @GetMapping("/{runId}")
    public ResponseEntity<PayrollRun> getPayrollRun(@PathVariable Integer runId) {
        return ResponseEntity.ok(payrollService.getPayrollRun(runId));
    }
    @PostMapping("/initiate")
    @PreAuthorize("hasAnyRole('PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<PayrollRun> initiatePayroll(
            @RequestParam Integer month,
            @RequestParam Integer year,
            @RequestParam Integer initiatedBy) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(payrollService.initiatePayrollRun(month, year, initiatedBy));
    }
    @PostMapping("/{runId}/compute")
    @PreAuthorize("hasAnyRole('PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<PayrollRun> computePayroll(@PathVariable Integer runId) {
        return ResponseEntity.ok(payrollService.computePayroll(runId));
    }
    @PostMapping("/{runId}/approve")
    @PreAuthorize("hasAnyRole('FINANCE_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<PayrollRun> approvePayroll(
            @PathVariable Integer runId,
            @RequestParam Integer approvedBy) {
        return ResponseEntity.ok(payrollService.approvePayroll(runId, approvedBy));
    }
    @PostMapping("/{runId}/disburse")
    @PreAuthorize("hasAnyRole('FINANCE_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<PayrollRun> disbursePayroll(@PathVariable Integer runId) {
        return ResponseEntity.ok(payrollService.disbursePayroll(runId));
    }
    @GetMapping("/{runId}/payrolls")
    public ResponseEntity<List<Payroll>> getPayrollsByRun(@PathVariable Integer runId) {
        return ResponseEntity.ok(payrollService.getPayrollByRun(runId));
    }
    @GetMapping("/employee/{empId}")
    public ResponseEntity<List<Payroll>> getPayrollsByEmployee(@PathVariable Integer empId) {
        return ResponseEntity.ok(payrollService.getPayrollByEmployee(empId));
    }
}

