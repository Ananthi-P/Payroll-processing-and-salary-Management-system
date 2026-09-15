package com.example.payroll.controller;

import com.example.payroll.entity.Allowance;
import com.example.payroll.service.AllowanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/allowances")
@RequiredArgsConstructor
public class AllowanceController {
    private final AllowanceService service;

    @GetMapping
    public ResponseEntity<List<Allowance>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Allowance>> getByEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(service.getByEmployee(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Allowance> getById(@PathVariable Integer id) { return ResponseEntity.ok(service.getById(id)); }

    @PostMapping
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE','PAYROLL_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<Allowance> create(@RequestBody Allowance value) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(value));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_EXECUTIVE','PAYROLL_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<Allowance> update(@PathVariable Integer id, @RequestBody Allowance value) {
        return ResponseEntity.ok(service.update(id, value));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }
}
