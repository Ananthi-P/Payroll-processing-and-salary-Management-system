import {BrowserRouter,Routes,Route,Navigate} from "react-router-dom";
import {useAuth} from "./auth";
import Layout from "./components/Layout";
import {Login,Register} from "./pages/Auth";
import Dashboard from "./pages/Dashboard";
import Employees from "./pages/Employees";
import Departments from "./pages/Departments";
import Salary from "./pages/Salary";
import Attendance from "./pages/Attendance";
import Leaves from "./pages/Leaves";
import Payroll from "./pages/Payroll";
import Payslips from "./pages/Payslips";
import {Reports,Compliance} from "./pages/Analytics";

function Guard({children}){const {user}=useAuth();return user?children:<Navigate to="/login" replace/>}
export default function App(){return <BrowserRouter><Routes><Route path="/login" element={<Login/>}/><Route path="/register" element={<Register/>}/><Route path="/" element={<Guard><Layout/></Guard>}><Route index element={<Dashboard/>}/><Route path="employees" element={<Employees/>}/><Route path="departments" element={<Departments/>}/><Route path="salary" element={<Salary/>}/><Route path="attendance" element={<Attendance/>}/><Route path="leaves" element={<Leaves/>}/><Route path="payroll" element={<Payroll/>}/><Route path="payslips" element={<Payslips/>}/><Route path="compliance" element={<Compliance/>}/><Route path="reports" element={<Reports/>}/></Route><Route path="*" element={<Navigate to="/" replace/>}/></Routes></BrowserRouter>}
