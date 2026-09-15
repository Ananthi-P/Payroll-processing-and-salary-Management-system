// ============================================================
// FILE: src/main/java/com/payroll/entity/Payslip.java
// ============================================================
package com.example.payroll.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payslip")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payslip_id")
    private Integer payslipId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id", unique = true)
    @JsonIgnore
    private Payroll payroll;

    @Column(name = "generated_date")
    private LocalDate generatedDate;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "is_emailed")
    private Boolean isEmailed = false;

    @Column(name = "emailed_at")
    private LocalDateTime emailedAt;

    @PrePersist
    protected void onCreate() {
        generatedDate = LocalDate.now();
    }
}

