
// ============================================================
// FILE: src/main/java/com/payroll/dto/EmployeeDTO.java
// ============================================================
package com.example.payroll.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private String employeeName;
    private String designation;
    private String email;
    private String phone;
    private LocalDate joiningDate;
    private BigDecimal basicSalary;
    private Integer departmentId;
    private String pan;
    private String uan;
    private String esicNo;
}
