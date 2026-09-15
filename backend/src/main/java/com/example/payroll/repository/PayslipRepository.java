// ============================================================
// FILE: src/main/java/com/payroll/repository/PayslipRepository.java
// ============================================================
package com.example.payroll.repository;

import com.example.payroll.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Integer> {
    Optional<Payslip> findByPayrollPayrollId(Integer payrollId);
}

