// ============================================================
// FILE: src/main/java/com/payroll/repository/PayrollRepository.java
// ============================================================
package com.example.payroll.repository;

import com.example.payroll.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Integer> {
    List<Payroll> findByEmployeeEmployeeId(Integer employeeId);
    List<Payroll> findByPayrollRunRunId(Integer runId);
}

