package com.example.payroll.controller;

import com.example.payroll.dto.EmployeeDTO;
import com.example.payroll.entity.Employee;
import com.example.payroll.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
    @GetMapping("/active")
    public ResponseEntity<List<Employee>> getActiveEmployees() {
        return ResponseEntity.ok(employeeService.getActiveEmployees());
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE', 'PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(dto));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE', 'PAYROLL_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE', 'SYSTEM_ADMIN')")
    public ResponseEntity<Void> deactivateEmployee(@PathVariable Integer id) {
        employeeService.deactivateEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
