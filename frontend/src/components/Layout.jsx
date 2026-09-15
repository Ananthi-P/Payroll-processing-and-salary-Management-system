import { NavLink, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../auth";
import { can } from "../utils";
import {
  LayoutDashboard, Users, Building2, WalletCards, Clock3, CalendarDays,
  Calculator, FileText, ShieldCheck, BarChart3, LogOut, ChevronRight, Sparkles
} from "lucide-react";

const nav = [
  {to:"/", label:"Overview", icon:LayoutDashboard},
  {to:"/employees", label:"Employees", icon:Users},
  {to:"/departments", label:"Departments", icon:Building2},
  {to:"/salary", label:"Salary structure", icon:WalletCards, roles:["HR_EXECUTIVE","PAYROLL_ADMIN","SYSTEM_ADMIN"]},
  {to:"/attendance", label:"Attendance", icon:Clock3},
  {to:"/leaves", label:"Leave desk", icon:CalendarDays},
  {to:"/payroll", label:"Payroll runs", icon:Calculator},
  {to:"/payslips", label:"Payslips", icon:FileText},
  {to:"/compliance", label:"Compliance", icon:ShieldCheck},
  {to:"/reports", label:"Reports", icon:BarChart3},
];

export default function Layout() {
  const {user,logout}=useAuth();
  const loc=useLocation();
  const current=nav.find(n=>n.to===loc.pathname)?.label || "Overview";
  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark"><Sparkles size={18}/></div><div><b>PayFlow</b><small>Payroll OS</small></div></div>
      <div className="workspace"><span className="live-dot"/> Live workspace</div>
      <nav>{nav.filter(n=>!n.roles || can(user?.role,n.roles)).map(n=>
        <NavLink key={n.to} to={n.to} className={({isActive})=>isActive?"nav-item active":"nav-item"}>
          <n.icon size={18}/><span>{n.label}</span>{n.to===loc.pathname&&<ChevronRight size={14} className="nav-arrow"/>}
        </NavLink>
      )}</nav>
      <div className="sidebar-bottom">
        <div className="role-card"><div className="avatar small">{(user?.username||"U").slice(0,1).toUpperCase()}</div><div><b>{user?.username}</b><small>{user?.role?.replaceAll("_"," ")}</small></div></div>
        <button className="logout" onClick={logout}><LogOut size={17}/> Sign out</button>
      </div>
    </aside>
    <main className="main">
      <header className="topbar"><div><span className="eyebrow">PAYFLOW / CONTROL CENTER</span><h2>{current}</h2></div><div className="top-actions"><span className="secure">● API connected</span><div className="avatar">{(user?.username||"U").slice(0,1).toUpperCase()}</div></div></header>
      <div className="page"><Outlet/></div>
    </main>
  </div>
}
