// ============================================================
// FILE: src/main/java/com/payroll/entity/Allowance.java
// ============================================================
package com.example.payroll.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "allowance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allowance_id")
    private Integer allowanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "allowance_type", nullable = false, length = 50)
    private String allowanceType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "is_active")
    private Boolean isActive = true;
}

