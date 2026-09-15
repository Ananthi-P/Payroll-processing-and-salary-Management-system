package com.example.payroll.controller;

import com.example.payroll.entity.Deduction;
import com.example.payroll.service.DeductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/deductions")
@RequiredArgsConstructor
public class DeductionController {
    private final DeductionService service;

    @GetMapping
    public ResponseEntity<List<Deduction>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Deduction>> getByEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(service.getByEmployee(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deduction> getById(@PathVariable Integer id) { return ResponseEntity.ok(service.getById(id)); }

    @PostMapping
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE','PAYROLL_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<Deduction> create(@RequestBody Deduction value) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(value));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE','PAYROLL_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<Deduction> update(@PathVariable Integer id, @RequestBody Deduction value) {
        return ResponseEntity.ok(service.update(id, value));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }
}
