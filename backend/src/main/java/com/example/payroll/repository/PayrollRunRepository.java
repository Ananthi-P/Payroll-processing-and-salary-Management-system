// ============================================================
// FILE: src/main/java/com/payroll/repository/PayrollRunRepository.java
// ============================================================
package com.example.payroll.repository;

import com.example.payroll.entity.PayrollRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PayrollRunRepository extends JpaRepository<PayrollRun, Integer> {
    Optional<PayrollRun> findByMonthAndYear(Integer month, Integer year);
    boolean existsByMonthAndYear(Integer month, Integer year);
}

