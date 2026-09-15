// ============================================================
// FILE: src/main/java/com/payroll/entity/Payroll.java
// ============================================================
package com.example.payroll.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_id")
    private Integer payrollId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id")
    private PayrollRun payrollRun;

    @Column(name = "payroll_month", length = 20)
    private String payrollMonth;

    @Column(name = "basic_earned", precision = 12, scale = 2)
    private BigDecimal basicEarned;

    @Column(name = "hra_earned", precision = 12, scale = 2)
    private BigDecimal hraEarned;

    @Column(name = "other_earnings", precision = 12, scale = 2)
    private BigDecimal otherEarnings;

    @Column(name = "gross_salary", precision = 12, scale = 2)
    private BigDecimal grossSalary;

    @Column(name = "pf_employee", precision = 10, scale = 2)
    private BigDecimal pfEmployee = BigDecimal.ZERO;

    @Column(name = "pf_employer", precision = 10, scale = 2)
    private BigDecimal pfEmployer = BigDecimal.ZERO;

    @Column(name = "esi_employee", precision = 10, scale = 2)
    private BigDecimal esiEmployee = BigDecimal.ZERO;

    @Column(name = "esi_employer", precision = 10, scale = 2)
    private BigDecimal esiEmployer = BigDecimal.ZERO;

    @Column(name = "professional_tax", precision = 10, scale = 2)
    private BigDecimal professionalTax = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal tds = BigDecimal.ZERO;

    @Column(name = "other_deductions", precision = 10, scale = 2)
    private BigDecimal otherDeductions = BigDecimal.ZERO;

    @Column(name = "total_deductions", precision = 10, scale = 2)
    private BigDecimal totalDeductions;

    @Column(name = "net_salary", precision = 12, scale = 2)
    private BigDecimal netSalary;

    @Column(name = "lop_days", precision = 4, scale = 1)
    private BigDecimal lopDays = BigDecimal.ZERO;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus = "PENDING";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "payroll", cascade = CascadeType.ALL)
    private Payslip payslip;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

