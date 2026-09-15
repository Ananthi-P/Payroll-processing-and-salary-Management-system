# PayFlow — Payroll Processing & Salary Management

This package contains the integrated React frontend and the supplied Spring Boot backend.

## Architecture

- `frontend/` — Vite + React UI
- `backend/` — Spring Boot API + PostgreSQL/Flyway
- Frontend API base: `http://localhost:8080/api/v1`
- Frontend dev server: `http://localhost:5173`

## Modules wired to backend

1. Login / Register → `/auth`
2. Dashboard → live Employees, Departments and Payroll Runs
3. Employees → GET/POST/PUT/DELETE
4. Departments → GET/POST/PUT/DELETE
5. Salary Structure → GET/POST/PUT/DELETE (new backend controller/service)
6. Allowances → GET/POST/PUT/DELETE (new backend controller/service)
7. Deductions → GET/POST/PUT/DELETE (new backend controller/service)
8. Attendance → GET by employee/month, GET monthly, POST
9. Leave Desk → GET, POST, approve/reject
10. Payroll Runs → initiate, compute, approve, disburse, inspect payroll rows
11. Payslips → generate, bulk generate, retrieve via payroll, mark emailed
12. Compliance → live checks from employee/payroll data
13. Reports → live aggregates from backend data

## Run backend

1. Make sure PostgreSQL is running on port `5433`.
2. Ensure database `payroll_db` exists and credentials in `backend/src/main/resources/application.properties` are correct.
3. From `backend/`:
   - Windows: `mvnw.cmd spring-boot:run`
   - Or: `mvn spring-boot:run`

## Run frontend

From `frontend/`:

```bash
npm install
npm run dev
```

Open `http://localhost:5173`.

If your backend runs somewhere else, create `.env` from `.env.example` and set:

```env
VITE_API_BASE_URL=http://YOUR_HOST:8080/api/v1
```

## Demo accounts from the supplied migration

- `admin / password123` — SYSTEM_ADMIN
- `suresh / password123` — EMPLOYEE
- `lakshmi / password123` — HR_EXECUTIVE
- `ravi / password123` — FINANCE_MANAGER
- `deepa / password123` — PAYROLL_ADMIN

## Important backend behavior

Payroll calculation remains server-side. The frontend does not duplicate PF/ESI/TDS logic; it sends workflow actions to the Spring Boot service, which persists the resulting payroll data.

The existing backend did not expose CRUD controllers for salary structures, allowances and deductions, so this package adds those API layers. The auth response also exposes the app user id so workflow foreign keys use the correct user record. It also updates CORS to allow the Vite frontend port `5173`.
