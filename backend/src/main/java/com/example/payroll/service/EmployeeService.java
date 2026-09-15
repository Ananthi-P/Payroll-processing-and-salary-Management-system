
// ============================================================
// FILE: src/main/java/com/example/payroll/service/EmployeeService.java
// ============================================================
package com.example.payroll.service;

import com.example.payroll.dto.EmployeeDTO;
import com.example.payroll.entity.Department;
import com.example.payroll.entity.Employee;
import com.example.payroll.exception.ResourceNotFoundException;
import com.example.payroll.repository.DepartmentRepository;
import com.example.payroll.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByStatus("ACTIVE");
    }
    public Employee createEmployee(EmployeeDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Employee employee = Employee.builder()
                .employeeName(dto.getEmployeeName())
                .designation(dto.getDesignation())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .joiningDate(dto.getJoiningDate())
                .basicSalary(dto.getBasicSalary())
                .department(department)
                .pan(dto.getPan())
                .uan(dto.getUan())
                .esicNo(dto.getEsicNo())
                .status("ACTIVE")
                .build();

        return employeeRepository.save(employee);
    }
    public Employee updateEmployee(Integer id, EmployeeDTO dto) {
        Employee employee = getEmployeeById(id);
        employee.setEmployeeName(dto.getEmployeeName());
        employee.setDesignation(dto.getDesignation());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setBasicSalary(dto.getBasicSalary());
        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            employee.setDepartment(dept);
        }
        return employeeRepository.save(employee);
    }

    public void deactivateEmployee(Integer id) {
        Employee employee = getEmployeeById(id);
        employee.setStatus("TERMINATED");
        employeeRepository.save(employee);
    }
}
