// ============================================================
// FILE: src/main/java/com/payroll/repository/LeaveRepository.java
// ============================================================
package com.example.payroll.repository;

import com.example.payroll.entity.LeaveRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRecord, Integer> {
    List<LeaveRecord> findByEmployeeEmployeeId(Integer employeeId);
    List<LeaveRecord> findByEmployeeEmployeeIdAndApprovalStatus(Integer employeeId, String status);
}

