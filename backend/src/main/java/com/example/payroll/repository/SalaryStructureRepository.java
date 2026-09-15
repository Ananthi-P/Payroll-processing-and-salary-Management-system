// ============================================================
// FILE: src/main/java/com/example/payroll/repository/SalaryStructureRepository.java
// ============================================================
package com.example.payroll.repository;
import com.example.payroll.entity.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Integer> {
    Optional<SalaryStructure> findByEmployeeEmployeeIdAndIsActiveTrue(Integer employeeId);
}

