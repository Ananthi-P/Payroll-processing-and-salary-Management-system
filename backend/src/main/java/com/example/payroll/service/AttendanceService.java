
// ============================================================
// FILE: src/main/java/com/example/payroll/service/AttendanceService.java
// ============================================================
package com.example.payroll.service;

import com.example.payroll.entity.Attendance;
import com.example.payroll.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<Attendance> getAttendanceByEmployeeAndMonth(Integer empId, Integer month, Integer year) {
        return attendanceRepository.findByEmployeeEmployeeIdAndMonthAndYear(empId, month, year);
    }

    public List<Attendance> getMonthlyAttendance(Integer month, Integer year) {
        return attendanceRepository.findByMonthAndYear(month, year);
    }

    public Attendance recordAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> importMonthlyAttendance(List<Attendance> attendanceList) {
        return attendanceRepository.saveAll(attendanceList);
    }
}
