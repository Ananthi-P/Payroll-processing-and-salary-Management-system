// ============================================================
// FILE: src/main/java/com/payroll/repository/DeductionRepository.java
// ============================================================
package com.example.payroll.repository;

import com.example.payroll.entity.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Integer> {
    List<Deduction> findByEmployeeEmployeeIdAndIsActiveTrue(Integer employeeId);
}

