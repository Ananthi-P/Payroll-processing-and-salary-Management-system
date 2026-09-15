// ============================================================
// FILE: src/main/java/com/payroll/repository/AttendanceRepository.java
// ============================================================
package com.example.payroll.repository;

import com.example.payroll.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByEmployeeEmployeeIdAndMonthAndYear(Integer employeeId, Integer month, Integer year);
    List<Attendance> findByMonthAndYear(Integer month, Integer year);
    Optional<Attendance> findByEmployeeEmployeeIdAndMonthAndYearAndAttendanceDateIsNull(
            Integer employeeId, Integer month, Integer year);
}

