
// ============================================================
// FILE: src/main/java/com/example/payroll/service/DepartmentService.java
// ============================================================
package com.example.payroll.service;

import com.example.payroll.entity.Department;
import com.example.payroll.exception.ResourceNotFoundException;
import com.example.payroll.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    public Department getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }
    public Department updateDepartment(Integer id, Department departmentDetails) {
        Department department = getDepartmentById(id);
        department.setDepartmentName(departmentDetails.getDepartmentName());
        department.setLocation(departmentDetails.getLocation());
        department.setManagerName(departmentDetails.getManagerName());
        return departmentRepository.save(department);
    }
    public void deleteDepartment(Integer id) {
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
    }
}
