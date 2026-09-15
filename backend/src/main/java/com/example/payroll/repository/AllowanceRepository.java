// ============================================================
// FILE: src/main/java/com/payroll/repository/AllowanceRepository.java
// ============================================================
package com.example.payroll.repository;

import com.example.payroll.entity.Allowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AllowanceRepository extends JpaRepository<Allowance, Integer> {
    List<Allowance> findByEmployeeEmployeeIdAndIsActiveTrue(Integer employeeId);
}

