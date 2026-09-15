package com.example.payroll.controller;

import com.example.payroll.entity.SalaryStructure;
import com.example.payroll.service.SalaryStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/salary-structures")
@RequiredArgsConstructor
public class SalaryStructureController {
    private final SalaryStructureService service;

    @GetMapping
    public ResponseEntity<List<SalaryStructure>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<SalaryStructure> getById(@PathVariable Integer id) { return ResponseEntity.ok(service.getById(id)); }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<SalaryStructure>> getByEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(service.getByEmployee(employeeId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE','PAYROLL_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<SalaryStructure> create(@RequestBody SalaryStructure value) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(value));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE','PAYROLL_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<SalaryStructure> update(@PathVariable Integer id, @RequestBody SalaryStructure value) {
        return ResponseEntity.ok(service.update(id, value));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }
}
