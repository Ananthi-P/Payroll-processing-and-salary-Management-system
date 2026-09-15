// ============================================================
// FILE: src/main/java/com/example/payroll/controller/PayslipController.java
// ============================================================
package com.example.payroll.controller;
import com.example.payroll.entity.Payslip;
import com.example.payroll.service.PayslipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/payslips")
@RequiredArgsConstructor
public class PayslipController {
    private final PayslipService payslipService;
    @PostMapping("/generate/{payrollId}")
    @PreAuthorize("hasAnyRole('PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<Payslip> generatePayslip(@PathVariable Integer payrollId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(payslipService.generatePayslip(payrollId));
    }
    @PostMapping("/generate-bulk/{runId}")
    @PreAuthorize("hasAnyRole('PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<String> generateBulkPayslips(@PathVariable Integer runId) {
        payslipService.generateBulkPayslips(runId);
        return ResponseEntity.ok("Payslips generated successfully for run: " + runId);
    }
    @GetMapping("/payroll/{payrollId}")
    public ResponseEntity<Payslip> getPayslipByPayroll(@PathVariable Integer payrollId) {
        return ResponseEntity.ok(payslipService.getPayslipByPayrollId(payrollId));
    }
    @PutMapping("/{payslipId}/email")
    @PreAuthorize("hasAnyRole('PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<Payslip> markAsEmailed(@PathVariable Integer payslipId) {
        return ResponseEntity.ok(payslipService.markAsEmailed(payslipId));
    }
}

