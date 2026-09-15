
// ============================================================
// FILE: src/main/java/com/payroll/dto/LoginRequest.java
// ============================================================
package com.example.payroll.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
