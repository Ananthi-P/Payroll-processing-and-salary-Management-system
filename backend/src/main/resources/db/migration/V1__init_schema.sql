
-- ============================================================
-- Flyway Migration V1: Initial Schema Creation
-- Location: src/main/resources/db/migration/V1__init_schema.sql
-- ============================================================

-- Department Table
CREATE TABLE department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    manager_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Employee Table
CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    designation VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    joining_date DATE,
    basic_salary DECIMAL(10,2),
    department_id INT REFERENCES department(department_id),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    pan VARCHAR(255),
    aadhaar_hash VARCHAR(255),
    uan VARCHAR(12),
    esic_no VARCHAR(17),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User Table (Authentication)
CREATE TABLE app_user (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'EMPLOYEE',
    employee_id INT UNIQUE REFERENCES employee(employee_id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Attendance Table
CREATE TABLE attendance (
    attendance_id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employee(employee_id),
    attendance_date DATE,
    month INT CHECK (month BETWEEN 1 AND 12),
    year INT,
    check_in TIME,
    check_out TIME,
    status VARCHAR(20) DEFAULT 'PRESENT',
    present_days DECIMAL(4,1),
    absent_days DECIMAL(4,1),
    lop_days DECIMAL(4,1) DEFAULT 0,
    ot_hours DECIMAL(5,2) DEFAULT 0,
    imported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Leave Table
CREATE TABLE leave_record (
    leave_id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employee(employee_id),
    leave_type VARCHAR(50),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    approval_status VARCHAR(20) DEFAULT 'PENDING',
    approved_by INT REFERENCES app_user(user_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Allowance Table
CREATE TABLE allowance (
    allowance_id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employee(employee_id),
    allowance_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    effective_from DATE,
    is_active BOOLEAN DEFAULT TRUE
);

-- Deduction Table
CREATE TABLE deduction (
    deduction_id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employee(employee_id),
    deduction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    effective_from DATE,
    is_active BOOLEAN DEFAULT TRUE
);

-- Salary Structure Table
CREATE TABLE salary_structure (
    structure_id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employee(employee_id),
    basic DECIMAL(12,2) NOT NULL,
    hra_pct DECIMAL(5,2) DEFAULT 40.00,
    da DECIMAL(12,2) DEFAULT 0,
    special_allowance DECIMAL(12,2) DEFAULT 0,
    lta_annual DECIMAL(12,2) DEFAULT 0,
    medical_allowance DECIMAL(12,2) DEFAULT 0,
    gross_ctc DECIMAL(12,2),
    effective_from DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Payroll Run Table
CREATE TABLE payroll_run (
    run_id SERIAL PRIMARY KEY,
    month INT NOT NULL CHECK (month BETWEEN 1 AND 12),
    year INT NOT NULL,
    status VARCHAR(20) DEFAULT 'DRAFT',
    initiated_by INT REFERENCES app_user(user_id),
    approved_by INT REFERENCES app_user(user_id),
    initiated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP,
    disbursed_at TIMESTAMP,
    total_gross DECIMAL(15,2) DEFAULT 0,
    total_net DECIMAL(15,2) DEFAULT 0,
    employee_count INT DEFAULT 0,
    UNIQUE(month, year)
);

-- Payroll (Per-Employee Payroll Details)
CREATE TABLE payroll (
    payroll_id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employee(employee_id),
    run_id INT REFERENCES payroll_run(run_id),
    payroll_month VARCHAR(20),
    basic_earned DECIMAL(12,2),
    hra_earned DECIMAL(12,2),
    other_earnings DECIMAL(12,2),
    gross_salary DECIMAL(12,2),
    pf_employee DECIMAL(10,2) DEFAULT 0,
    pf_employer DECIMAL(10,2) DEFAULT 0,
    esi_employee DECIMAL(10,2) DEFAULT 0,
    esi_employer DECIMAL(10,2) DEFAULT 0,
    professional_tax DECIMAL(10,2) DEFAULT 0,
    tds DECIMAL(10,2) DEFAULT 0,
    other_deductions DECIMAL(10,2) DEFAULT 0,
    total_deductions DECIMAL(10,2),
    net_salary DECIMAL(12,2),
    lop_days DECIMAL(4,1) DEFAULT 0,
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Payslip Table
CREATE TABLE payslip (
    payslip_id SERIAL PRIMARY KEY,
    payroll_id INT UNIQUE REFERENCES payroll(payroll_id),
    generated_date DATE DEFAULT CURRENT_DATE,
    file_path VARCHAR(500),
    is_emailed BOOLEAN DEFAULT FALSE,
    emailed_at TIMESTAMP
);

-- Performance Indexes
CREATE INDEX idx_employee_department ON employee(department_id);
CREATE INDEX idx_employee_email ON employee(email);
CREATE INDEX idx_attendance_emp_month ON attendance(employee_id, month, year);
CREATE INDEX idx_payroll_emp ON payroll(employee_id);
CREATE INDEX idx_payroll_run ON payroll(run_id);
CREATE INDEX idx_leave_emp ON leave_record(employee_id);

