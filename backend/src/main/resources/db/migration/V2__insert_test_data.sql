-- Insert Departments
INSERT INTO department (department_name, location, manager_name) VALUES
('Engineering', 'Bangalore', 'Rajesh Kumar'),
('Human Resources', 'Chennai', 'Priya Sharma'),
('Finance', 'Mumbai', 'Anil Mehta');

-- Insert Employees
INSERT INTO employee (employee_name, designation, email, phone, joining_date, basic_salary, department_id, status, uan) VALUES
('Admin User', 'System Administrator', 'admin@payroll.com', '9876543210', '2020-01-01', 80000.00, 1, 'ACTIVE', '100123456789'),
('Suresh Kumar', 'Software Engineer', 'suresh@payroll.com', '9876543211', '2022-06-15', 45000.00, 1, 'ACTIVE', '100123456790'),
('Lakshmi Devi', 'HR Executive', 'lakshmi@payroll.com', '9876543212', '2021-03-10', 40000.00, 2, 'ACTIVE', '100123456791'),
('Ravi Shankar', 'Finance Manager', 'ravi@payroll.com', '9876543213', '2019-08-20', 70000.00, 3, 'ACTIVE', '100123456792'),
('Deepa Nair', 'Senior Developer', 'deepa@payroll.com', '9876543214', '2021-11-01', 55000.00, 1, 'ACTIVE', '100123456793');

-- Insert Users (password = "password123")
INSERT INTO app_user (username, password, role, employee_id, is_active) VALUES
('admin', '$2a$12$LQv3c1yqBo9SkvXS7QTJPOoGz2Z2V5qR5HQDnBJ2bAnWfDmTlGIGi', 'SYSTEM_ADMIN', 1, true),
('suresh', '$2a$12$LQv3c1yqBo9SkvXS7QTJPOoGz2Z2V5qR5HQDnBJ2bAnWfDmTlGIGi', 'EMPLOYEE', 2, true),
('lakshmi', '$2a$12$LQv3c1yqBo9SkvXS7QTJPOoGz2Z2V5qR5HQDnBJ2bAnWfDmTlGIGi', 'HR_EXECUTIVE', 3, true),
('ravi', '$2a$12$LQv3c1yqBo9SkvXS7QTJPOoGz2Z2V5qR5HQDnBJ2bAnWfDmTlGIGi', 'FINANCE_MANAGER', 4, true),
('deepa', '$2a$12$LQv3c1yqBo9SkvXS7QTJPOoGz2Z2V5qR5HQDnBJ2bAnWfDmTlGIGi', 'PAYROLL_ADMIN', 5, true);

-- Insert Salary Structures
INSERT INTO salary_structure (employee_id, basic, hra_pct, da, special_allowance, lta_annual, medical_allowance, gross_ctc, effective_from, is_active) VALUES
(1, 80000.00, 40.00, 5000.00, 15000.00, 24000.00, 1250.00, 150000.00, '2020-01-01', true),
(2, 45000.00, 40.00, 3000.00, 8000.00, 12000.00, 1250.00, 85000.00, '2022-06-15', true),
(3, 40000.00, 40.00, 2500.00, 7000.00, 10000.00, 1250.00, 75000.00, '2021-03-10', true),
(4, 70000.00, 40.00, 4500.00, 12000.00, 20000.00, 1250.00, 130000.00, '2019-08-20', true),
(5, 55000.00, 40.00, 3500.00, 10000.00, 15000.00, 1250.00, 100000.00, '2021-11-01', true);

-- Insert Attendance for August 2026
INSERT INTO attendance (employee_id, month, year, present_days, absent_days, lop_days, ot_hours) VALUES
(1, 8, 2026, 28, 2, 0, 0),
(2, 8, 2026, 26, 4, 2, 5.5),
(3, 8, 2026, 27, 3, 1, 0),
(4, 8, 2026, 29, 1, 0, 0),
(5, 8, 2026, 25, 5, 3, 8.0);
