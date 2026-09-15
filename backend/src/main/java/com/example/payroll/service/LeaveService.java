
// ============================================================
// FILE: src/main/java/com/example/payroll/service/LeaveService.java
// ============================================================
package com.example.payroll.service;

import com.example.payroll.entity.LeaveRecord;
import com.example.payroll.exception.ResourceNotFoundException;
import com.example.payroll.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {
    private final LeaveRepository leaveRepository;
    public List<LeaveRecord> getLeavesByEmployee(Integer employeeId) {
        return leaveRepository.findByEmployeeEmployeeId(employeeId);
    }
    public LeaveRecord applyLeave(LeaveRecord leaveRecord) {
        return leaveRepository.save(leaveRecord);
    }
    public LeaveRecord approveLeave(Integer leaveId, Integer approvedBy) {
        LeaveRecord leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave record not found with id: " + leaveId));
        leave.setApprovalStatus("APPROVED");
        leave.setApprovedBy(approvedBy);
        return leaveRepository.save(leave);
    }
    public LeaveRecord rejectLeave(Integer leaveId, Integer rejectedBy) {
        LeaveRecord leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave record not found with id: " + leaveId));
        leave.setApprovalStatus("REJECTED");
        leave.setApprovedBy(rejectedBy);
        return leaveRepository.save(leave);
    }
    public List<LeaveRecord> getPendingLeaves(Integer employeeId) {
        return leaveRepository.findByEmployeeEmployeeIdAndApprovalStatus(employeeId, "PENDING");
    }
}
