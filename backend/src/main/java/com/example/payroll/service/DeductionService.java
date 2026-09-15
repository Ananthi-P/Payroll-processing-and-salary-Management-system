package com.example.payroll.service;

import com.example.payroll.entity.Deduction;
import com.example.payroll.exception.ResourceNotFoundException;
import com.example.payroll.repository.DeductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeductionService {
    private final DeductionRepository repository;

    public List<Deduction> getAll() { return repository.findAll(); }

    public List<Deduction> getByEmployee(Integer employeeId) {
        return repository.findByEmployeeEmployeeIdAndIsActiveTrue(employeeId);
    }

    public Deduction getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction not found: " + id));
    }

    public Deduction create(Deduction value) { return repository.save(value); }

    public Deduction update(Integer id, Deduction incoming) {
        Deduction current = getById(id);
        current.setDeductionType(incoming.getDeductionType());
        current.setAmount(incoming.getAmount());
        current.setEffectiveFrom(incoming.getEffectiveFrom());
        current.setIsActive(incoming.getIsActive());
        if (incoming.getEmployee() != null) current.setEmployee(incoming.getEmployee());
        return repository.save(current);
    }

    public void delete(Integer id) { repository.delete(getById(id)); }
}
