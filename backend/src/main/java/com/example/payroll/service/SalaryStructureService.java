package com.example.payroll.service;

import com.example.payroll.entity.SalaryStructure;
import com.example.payroll.exception.ResourceNotFoundException;
import com.example.payroll.repository.SalaryStructureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryStructureService {
    private final SalaryStructureRepository repository;

    public List<SalaryStructure> getAll() { return repository.findAll(); }

    public SalaryStructure getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary structure not found: " + id));
    }

    public List<SalaryStructure> getByEmployee(Integer employeeId) {
        return repository.findAll().stream()
                .filter(s -> s.getEmployee() != null &&
                        employeeId.equals(s.getEmployee().getEmployeeId()))
                .toList();
    }

    public SalaryStructure create(SalaryStructure value) { return repository.save(value); }

    public SalaryStructure update(Integer id, SalaryStructure incoming) {
        SalaryStructure current = getById(id);
        current.setBasic(incoming.getBasic());
        current.setHraPct(incoming.getHraPct());
        current.setDa(incoming.getDa());
        current.setSpecialAllowance(incoming.getSpecialAllowance());
        current.setLtaAnnual(incoming.getLtaAnnual());
        current.setMedicalAllowance(incoming.getMedicalAllowance());
        current.setGrossCtc(incoming.getGrossCtc());
        current.setEffectiveFrom(incoming.getEffectiveFrom());
        current.setIsActive(incoming.getIsActive());
        if (incoming.getEmployee() != null) current.setEmployee(incoming.getEmployee());
        return repository.save(current);
    }

    public void delete(Integer id) { repository.delete(getById(id)); }
}
