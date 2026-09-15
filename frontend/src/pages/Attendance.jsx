import { useEffect, useState } from "react";
import { useAuth } from "../auth";
import { today, can } from "../utils";
import {
 Plus,
 Clock3,
} from "lucide-react";

import { api } from "../api";
import {
 Button,
 Card,
 Field,
 Input,
 Select,
 Modal,
 Empty,
 Badge,
} from "../components/UI";

const blank = {
 employeeId: "",
 attendanceDate: today(),
 month: new Date().getMonth() + 1,
 year: new Date().getFullYear(),
 checkIn: "09:30",
 checkOut: "18:00",
 status: "PRESENT",
 presentDays: "1",
 absentDays: "0",
 lopDays: "0",
 otHours: "0",
};

export default function Attendance() {
 const { user } = useAuth();

 const [employees, setEmployees] = useState([]);
 const [items, setItems] = useState([]);
 const [form, setForm] = useState(blank);

 const [open, setOpen] = useState(false);
 const [loading, setLoading] = useState(false);
 const [err, setErr] = useState("");

 const [filter, setFilter] = useState({
  month: new Date().getMonth() + 1,
  year: new Date().getFullYear(),
  employeeId: "",
 });

 /*
  * Attendance permissions
  *
  * HR_EXECUTIVE:
  * - Can view monthly attendance
  * - Can record attendance
  *
  * PAYROLL_ADMIN:
  * - Can view monthly attendance
  * - Can record attendance
  *
  * SYSTEM_ADMIN:
  * - Full attendance access
  *
  * EMPLOYEE:
  * - Cannot access monthly attendance endpoint
  * - Cannot record attendance
  */
 const canViewMonthly = can(user?.role, [
  "HR_EXECUTIVE",
  "PAYROLL_ADMIN",
  "SYSTEM_ADMIN",
 ]);

 const canManageAttendance = can(user?.role, [
  "HR_EXECUTIVE",
  "PAYROLL_ADMIN",
  "SYSTEM_ADMIN",
 ]);

 /*
  * Load employees and attendance records
  */
 const load = async () => {
  try {
   setErr("");

   /*
    * Employees are needed for the dropdown.
    */
   const employeeData = await api.get("/employees");
   setEmployees(employeeData || []);

   let path;

   /*
    * If a particular employee is selected,
    * use the employee-specific endpoint.
    *
    * Otherwise use the monthly endpoint only
    * when the logged-in user has permission.
    */
   if (filter.employeeId) {
    path =
        `/attendance/employee/${filter.employeeId}` +
        `?month=${filter.month}&year=${filter.year}`;
   } else if (canViewMonthly) {
    path =
        `/attendance/monthly` +
        `?month=${filter.month}&year=${filter.year}`;
   } else {
    /*
     * EMPLOYEE users should not call the
     * restricted /attendance/monthly endpoint.
     */
    setItems([]);
    return;
   }

   const attendanceData = await api.get(path);

   setItems(attendanceData || []);
  } catch (e) {
   setErr(e.message || "Failed to load attendance records.");
   setItems([]);
  }
 };

 /*
  * Reload when filters or permissions change.
  */
 useEffect(() => {
  if (user) {
   load();
  }
 }, [
  filter.month,
  filter.year,
  filter.employeeId,
  user?.role,
 ]);

 /*
  * Save attendance
  */
 const save = async (e) => {
  e.preventDefault();

  if (!canManageAttendance) {
   setErr(
       "You do not have permission to record attendance."
   );
   return;
  }

  if (!form.employeeId) {
   setErr("Please select an employee.");
   return;
  }

  setLoading(true);
  setErr("");

  try {
   const payload = {
    attendanceDate: form.attendanceDate,

    month: Number(form.month),
    year: Number(form.year),

    checkIn: form.checkIn || null,
    checkOut: form.checkOut || null,

    status: form.status,

    presentDays: Number(form.presentDays || 0),
    absentDays: Number(form.absentDays || 0),
    lopDays: Number(form.lopDays || 0),
    otHours: Number(form.otHours || 0),

    employee: {
     employeeId: Number(form.employeeId),
    },
   };

   await api.post("/attendance", payload);

   setOpen(false);

   /*
    * Reset the form after successful save.
    */
   setForm({
    ...blank,
    attendanceDate: today(),
    month: filter.month,
    year: filter.year,
   });

   await load();
  } catch (x) {
   setErr(
       x.message || "Failed to record attendance."
   );
  } finally {
   setLoading(false);
  }
 };

 /*
  * Update filter helper
  */
 const updateFilter = (field, value) => {
  setFilter((previous) => ({
   ...previous,
   [field]: value,
  }));
 };

 /*
  * Update form helper
  */
 const updateForm = (field, value) => {
  setForm((previous) => ({
   ...previous,
   [field]: value,
  }));
 };

 return (
     <div className="fade-in">

      {/* ================================
          PAGE HEADER
      ================================= */}
      <div className="page-head">

       <div>
          <span className="eyebrow">
            TIME & PRESENCE
          </span>

        <h1>Attendance</h1>

        <p className="muted">
         Record attendance and feed LOP/OT data
         into payroll computation.
        </p>
       </div>

       {canManageAttendance && (
           <Button
               icon={Plus}
               onClick={() => {
                setErr("");

                setForm({
                 ...blank,
                 attendanceDate: today(),
                 month: filter.month,
                 year: filter.year,
                });

                setOpen(true);
               }}
           >
            Record attendance
           </Button>
       )}
      </div>

      {/* ================================
          ERROR MESSAGE
      ================================= */}
      {err && (
          <div className="error-box">
           {err}
          </div>
      )}

      {/* ================================
          FILTERS
      ================================= */}
      <Card>
       <div className="filter-row">

        <Field label="Employee">
         <Select
             value={filter.employeeId}
             onChange={(e) =>
                 updateFilter(
                     "employeeId",
                     e.target.value
                 )
             }
         >
          <option value="">
           All employees
          </option>

          {employees.map((employee) => (
              <option
                  key={employee.employeeId}
                  value={employee.employeeId}
              >
               {employee.employeeName}
              </option>
          ))}
         </Select>
        </Field>

        <Field label="Month">
         <Input
             type="number"
             min="1"
             max="12"
             value={filter.month}
             onChange={(e) =>
                 updateFilter(
                     "month",
                     e.target.value
                 )
             }
         />
        </Field>

        <Field label="Year">
         <Input
             type="number"
             value={filter.year}
             onChange={(e) =>
                 updateFilter(
                     "year",
                     e.target.value
                 )
             }
         />
        </Field>

       </div>
      </Card>

      {/* ================================
          ATTENDANCE RECORDS
      ================================= */}
      <Card>

       <div className="section-head">

        <div>
            <span className="eyebrow">
              BACKEND RECORDS
            </span>

         <h3>
          {items.length} attendance entries
         </h3>
        </div>

        <Clock3 size={19} />

       </div>

       {items.length ? (

           <div className="data-table">

            <div className="tr th">
             <span>Date</span>
             <span>Employee</span>
             <span>Status</span>
             <span>Present</span>
             <span>LOP</span>
             <span>OT hours</span>
            </div>

            {items.map((x) => (

                <div
                    className="tr"
                    key={x.attendanceId}
                >

                <span>
                  {x.attendanceDate || "—"}
                </span>

                 <span>
                  <b>
                    {x.employee?.employeeName ||
                        `#${x.employee?.employeeId || "—"}`}
                  </b>
                </span>

                 <Badge>
                  {x.status || "—"}
                 </Badge>

                 <span>
                  {x.presentDays ?? "—"}
                </span>

                 <span>
                  {x.lopDays ?? 0}
                </span>

                 <span>
                  {x.otHours ?? 0}
                </span>

                </div>

            ))}

           </div>

       ) : (

           <Empty
               title="No attendance records"
               text={
                canManageAttendance
                    ? "Use Record attendance to create a backend row."
                    : "No attendance records are available for the selected filters."
               }
           />

       )}

      </Card>

      {/* ================================
          RECORD ATTENDANCE MODAL
      ================================= */}
      {open && canManageAttendance && (

          <Modal
              title="Record attendance"
              onClose={() => setOpen(false)}
          >

           <form onSubmit={save}>

            {/* Employee */}
            <Field label="Employee">

             <Select
                 required
                 value={form.employeeId}
                 onChange={(e) =>
                     updateForm(
                         "employeeId",
                         e.target.value
                     )
                 }
             >

              <option value="">
               Select employee
              </option>

              {employees.map((employee) => (

                  <option
                      value={employee.employeeId}
                      key={employee.employeeId}
                  >
                   {employee.employeeName}
                  </option>

              ))}

             </Select>

            </Field>

            {/* Date */}
            <Field label="Date">

             <Input
                 type="date"
                 required
                 value={form.attendanceDate}
                 onChange={(e) =>
                     updateForm(
                         "attendanceDate",
                         e.target.value
                     )
                 }
             />

            </Field>

            {/* Month / Year */}
            <div className="two">

             <Field label="Month">

              <Input
                  type="number"
                  min="1"
                  max="12"
                  required
                  value={form.month}
                  onChange={(e) =>
                      updateForm(
                          "month",
                          e.target.value
                      )
                  }
              />

             </Field>

             <Field label="Year">

              <Input
                  type="number"
                  required
                  value={form.year}
                  onChange={(e) =>
                      updateForm(
                          "year",
                          e.target.value
                      )
                  }
              />

             </Field>

            </div>

            {/* Check In / Check Out */}
            <div className="two">

             <Field label="Check in">

              <Input
                  type="time"
                  value={form.checkIn}
                  onChange={(e) =>
                      updateForm(
                          "checkIn",
                          e.target.value
                      )
                  }
              />

             </Field>

             <Field label="Check out">

              <Input
                  type="time"
                  value={form.checkOut}
                  onChange={(e) =>
                      updateForm(
                          "checkOut",
                          e.target.value
                      )
                  }
              />

             </Field>

            </div>

            {/* Status */}
            <Field label="Status">

             <Select
                 value={form.status}
                 onChange={(e) =>
                     updateForm(
                         "status",
                         e.target.value
                     )
                 }
             >

              {[
               "PRESENT",
               "ABSENT",
               "HALF_DAY",
               "ON_LEAVE",
              ].map((status) => (

                  <option
                      key={status}
                      value={status}
                  >
                   {status}
                  </option>

              ))}

             </Select>

            </Field>

            {/* Present / Absent */}
            <div className="two">

             <Field label="Present days">

              <Input
                  type="number"
                  step="0.5"
                  min="0"
                  value={form.presentDays}
                  onChange={(e) =>
                      updateForm(
                          "presentDays",
                          e.target.value
                      )
                  }
              />

             </Field>

             <Field label="Absent days">

              <Input
                  type="number"
                  step="0.5"
                  min="0"
                  value={form.absentDays}
                  onChange={(e) =>
                      updateForm(
                          "absentDays",
                          e.target.value
                      )
                  }
              />

             </Field>

            </div>

            {/* LOP / OT */}
            <div className="two">

             <Field label="LOP days">

              <Input
                  type="number"
                  step="0.5"
                  min="0"
                  value={form.lopDays}
                  onChange={(e) =>
                      updateForm(
                          "lopDays",
                          e.target.value
                      )
                  }
              />

             </Field>

             <Field label="OT hours">

              <Input
                  type="number"
                  step="0.25"
                  min="0"
                  value={form.otHours}
                  onChange={(e) =>
                      updateForm(
                          "otHours",
                          e.target.value
                      )
                  }
              />

             </Field>

            </div>

            {/* Modal buttons */}
            <div className="modal-actions">

             <Button
                 variant="ghost"
                 type="button"
                 onClick={() => setOpen(false)}
             >
              Cancel
             </Button>

             <Button
                 loading={loading}
                 type="submit"
             >
              Record attendance
             </Button>

            </div>

           </form>

          </Modal>

      )}

     </div>
 );
}