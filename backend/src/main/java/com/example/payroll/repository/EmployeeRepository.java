// ============================================================
// FILE: src/main/java/com/payroll/repository/EmployeeRepository.java
// ============================================================
package com.example.payroll.repository;

import com.example.payroll.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartmentDepartmentId(Integer departmentId);
    List<Employee> findByStatus(String status);
    boolean existsByEmail(String email);
}

