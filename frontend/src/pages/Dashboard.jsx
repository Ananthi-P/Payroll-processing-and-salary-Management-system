import { useEffect, useMemo, useState } from "react";
import { ArrowUpRight, Users, Wallet, CircleDollarSign, Activity, RefreshCw, Calculator, CheckCircle2, Clock3 } from "lucide-react";
import { api } from "../api";
import { money, monthName } from "../utils";
import { Card, Badge, Button } from "../components/UI";

export default function Dashboard() {
  const [data,setData]=useState({employees:[],runs:[],departments:[]}),[loading,setLoading]=useState(true),[err,setErr]=useState("");
  const load=async()=>{setLoading(true);try{const [employees,runs,departments]=await Promise.all([api.get("/employees"),api.get("/payroll-runs"),api.get("/departments")]);setData({employees,runs,departments})}catch(e){setErr(e.message)}finally{setLoading(false)}};
  useEffect(()=>{load()},[]);
  const active=data.employees.filter(e=>e.status==="ACTIVE").length;
  const latest=data.runs.slice().sort((a,b)=>(b.year-a.year)||(b.month-a.month))[0];
  const totalNet=data.runs.reduce((s,r)=>s+Number(r.totalNet||0),0);
  const totalGross=data.runs.reduce((s,r)=>s+Number(r.totalGross||0),0);
  const max=Math.max(...data.runs.map(r=>Number(r.totalNet||0)),1);
  return <div className="fade-in">
    <div className="welcome-row"><div><span className="pill dark">SEPTEMBER 2026</span><h1>Good evening. <em>Let's run payroll.</em></h1><p className="muted">A live view of your people, payroll runs and salary operations.</p></div><Button variant="ghost" icon={RefreshCw} onClick={load}>Refresh data</Button></div>
    {err&&<div className="error-box">{err}</div>}
    <div className="stat-grid">
      <Card><div className="stat-head"><span>ACTIVE PEOPLE</span><Users size={17}/></div><b className="stat-number">{active}</b><div className="trend">↑ Live from employees</div></Card>
      <Card><div className="stat-head"><span>PAYROLL RUNS</span><Calculator size={17}/></div><b className="stat-number">{data.runs.length}</b><div className="trend">{latest?`${monthName(latest.month)} ${latest.year} · ${latest.status}`:"No runs yet"}</div></Card>
      <Card><div className="stat-head"><span>GROSS PROCESSED</span><Wallet size={17}/></div><b className="stat-number">{money(totalGross)}</b><div className="trend">Across stored runs</div></Card>
      <Card><div className="stat-head"><span>NET PAYROLL</span><CircleDollarSign size={17}/></div><b className="stat-number">{money(totalNet)}</b><div className="trend">Backend-calculated</div></Card>
    </div>
    <div className="dashboard-grid">
      <Card className="chart-card"><div className="section-head"><div><span className="eyebrow">PAYROLL PULSE</span><h3>Net payroll history</h3></div><Activity size={19}/></div>
        {data.runs.length?<div className="bar-chart">{data.runs.slice().sort((a,b)=>a.runId-b.runId).slice(-8).map(r=><div className="bar-col" key={r.runId}><div className="bar-value">{money(r.totalNet)}</div><div className="bar" style={{height:`${Math.max(8,(Number(r.totalNet)/max)*160)}px`}}/><small>{monthName(r.month)}</small></div>)}</div>:<div className="empty compact"><div className="empty-orb">◌</div><strong>No payroll history</strong><p>Initiate your first run from Payroll Runs.</p></div>}
      </Card>
      <Card><div className="section-head"><div><span className="eyebrow">LATEST RUN</span><h3>Payroll status</h3></div><Clock3 size={19}/></div>
        {latest?<><div className="run-highlight"><div className="run-icon"><Calculator size={20}/></div><div><b>{monthName(latest.month)} {latest.year}</b><small>Run #{latest.runId}</small></div><Badge>{latest.status}</Badge></div><div className="detail-list"><div><span>Employees</span><b>{latest.employeeCount||0}</b></div><div><span>Gross</span><b>{money(latest.totalGross)}</b></div><div><span>Net</span><b>{money(latest.totalNet)}</b></div></div></>:<div className="empty compact"><strong>No run selected</strong><p>Start a monthly payroll run.</p></div>}
      </Card>
    </div>
    <Card className="activity-card"><div className="section-head"><div><span className="eyebrow">WORKFORCE</span><h3>People snapshot</h3></div><a href="/employees" className="text-link">View all <ArrowUpRight size={14}/></a></div>
      <div className="mini-table">{data.employees.slice(0,5).map(e=><div className="mini-row" key={e.employeeId}><div className="person"><div className="avatar">{e.employeeName?.slice(0,1)}</div><div><b>{e.employeeName}</b><small>{e.designation||"Employee"} · {e.department?.departmentName||"—"}</small></div></div><span>{money(e.basicSalary)}</span><Badge>{e.status}</Badge></div>)}</div>
    </Card>
  </div>
}
