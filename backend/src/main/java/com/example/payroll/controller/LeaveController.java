// ============================================================
// FILE: src/main/java/com/example/payroll/controller/LeaveController.java
// ============================================================
package com.example.payroll.controller;

import com.example.payroll.entity.LeaveRecord;
import com.example.payroll.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leaves")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService leaveService;
    @GetMapping("/employee/{empId}")
    public ResponseEntity<List<LeaveRecord>> getLeavesByEmployee(@PathVariable Integer empId) {
        return ResponseEntity.ok(leaveService.getLeavesByEmployee(empId));
    }
    @GetMapping("/employee/{empId}/pending")
    public ResponseEntity<List<LeaveRecord>> getPendingLeaves(@PathVariable Integer empId) {
        return ResponseEntity.ok(leaveService.getPendingLeaves(empId));
    }
    @PostMapping
    public ResponseEntity<LeaveRecord> applyLeave(@RequestBody LeaveRecord leaveRecord) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveService.applyLeave(leaveRecord));
    }
    @PutMapping("/{leaveId}/approve")
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE', 'PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<LeaveRecord> approveLeave(
            @PathVariable Integer leaveId,
            @RequestParam Integer approvedBy) {
        return ResponseEntity.ok(leaveService.approveLeave(leaveId, approvedBy));
    }
    @PutMapping("/{leaveId}/reject")
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE', 'PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<LeaveRecord> rejectLeave(
            @PathVariable Integer leaveId,
            @RequestParam Integer rejectedBy) {
        return ResponseEntity.ok(leaveService.rejectLeave(leaveId, rejectedBy));
    }
}

