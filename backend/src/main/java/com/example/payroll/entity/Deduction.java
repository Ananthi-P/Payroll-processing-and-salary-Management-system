// ============================================================
// FILE: src/main/java/com/payroll/entity/Deduction.java
// ============================================================
package com.example.payroll.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "deduction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduction_id")
    private Integer deductionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "deduction_type", nullable = false, length = 50)
    private String deductionType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "is_active")
    private Boolean isActive = true;
}

