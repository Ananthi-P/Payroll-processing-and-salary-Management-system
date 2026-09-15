
// ============================================================
// FILE: src/main/java/com/payroll/exception/PayrollAlreadyLockedException.java
// ============================================================
package com.example.payroll.exception;

public class PayrollAlreadyLockedException extends RuntimeException {
    public PayrollAlreadyLockedException(String message) {
        super(message);
    }
}
