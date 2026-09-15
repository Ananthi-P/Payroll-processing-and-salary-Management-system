package com.example.payroll.service;

import com.example.payroll.entity.Allowance;
import com.example.payroll.exception.ResourceNotFoundException;
import com.example.payroll.repository.AllowanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllowanceService {
    private final AllowanceRepository repository;

    public List<Allowance> getAll() { return repository.findAll(); }

    public List<Allowance> getByEmployee(Integer employeeId) {
        return repository.findByEmployeeEmployeeIdAndIsActiveTrue(employeeId);
    }

    public Allowance getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allowance not found: " + id));
    }

    public Allowance create(Allowance value) { return repository.save(value); }

    public Allowance update(Integer id, Allowance incoming) {
        Allowance current = getById(id);
        current.setAllowanceType(incoming.getAllowanceType());
        current.setAmount(incoming.getAmount());
        current.setEffectiveFrom(incoming.getEffectiveFrom());
        current.setIsActive(incoming.getIsActive());
        if (incoming.getEmployee() != null) current.setEmployee(incoming.getEmployee());
        return repository.save(current);
    }

    public void delete(Integer id) { repository.delete(getById(id)); }
}
