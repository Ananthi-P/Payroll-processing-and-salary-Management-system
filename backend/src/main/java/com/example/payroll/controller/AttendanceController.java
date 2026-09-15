// ============================================================
// FILE: src/main/java/com/example/payroll/controller/AttendanceController.java
// ============================================================
package com.example.payroll.controller;
import com.example.payroll.entity.Attendance;
import com.example.payroll.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;
    @GetMapping("/employee/{empId}")
    public ResponseEntity<List<Attendance>> getByEmployee(
            @PathVariable Integer empId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(attendanceService.getAttendanceByEmployeeAndMonth(empId, month, year));
    }
    @GetMapping("/monthly")
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE', 'PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<List<Attendance>> getMonthlyAttendance(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(attendanceService.getMonthlyAttendance(month, year));
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE', 'PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<Attendance> recordAttendance(@RequestBody Attendance attendance) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.recordAttendance(attendance));
    }
    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE', 'PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<List<Attendance>> importAttendance(@RequestBody List<Attendance> attendanceList) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.importMonthlyAttendance(attendanceList));
    }
}

