// ============================================================
// FILE: src/main/java/com/payroll/entity/PayrollRun.java
// ============================================================
package com.example.payroll.entity;

import com.example.payroll.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_run")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "run_id")
    private Integer runId;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PayrollStatus status = PayrollStatus.DRAFT;

    @Column(name = "initiated_by")
    private Integer initiatedBy;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "initiated_at")
    private LocalDateTime initiatedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "disbursed_at")
    private LocalDateTime disbursedAt;

    @Column(name = "total_gross", precision = 15, scale = 2)
    private BigDecimal totalGross = BigDecimal.ZERO;

    @Column(name = "total_net", precision = 15, scale = 2)
    private BigDecimal totalNet = BigDecimal.ZERO;

    @Column(name = "employee_count")
    private Integer employeeCount = 0;

    @PrePersist
    protected void onCreate() {
        initiatedAt = LocalDateTime.now();
    }
}

