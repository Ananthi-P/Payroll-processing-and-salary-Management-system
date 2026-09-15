// ============================================================
// FILE: src/main/java/com/payroll/entity/Attendance.java
// ============================================================
package com.example.payroll.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Integer attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    private Integer month;
    private Integer year;

    @Column(name = "check_in")
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    @Column(length = 20)
    private String status = "PRESENT";

    @Column(name = "present_days", precision = 4, scale = 1)
    private BigDecimal presentDays;

    @Column(name = "absent_days", precision = 4, scale = 1)
    private BigDecimal absentDays;

    @Column(name = "lop_days", precision = 4, scale = 1)
    private BigDecimal lopDays = BigDecimal.ZERO;

    @Column(name = "ot_hours", precision = 5, scale = 2)
    private BigDecimal otHours = BigDecimal.ZERO;

    @Column(name = "imported_at")
    private LocalDateTime importedAt;

    @PrePersist
    protected void onCreate() {
        importedAt = LocalDateTime.now();
    }
}

