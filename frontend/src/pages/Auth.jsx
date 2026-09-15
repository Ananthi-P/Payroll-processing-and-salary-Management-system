import { useState } from "react";
import { Navigate, useNavigate, Link } from "react-router-dom";
import { Sparkles, Eye, EyeOff, ShieldCheck, ArrowRight } from "lucide-react";
import { useAuth } from "../auth";
import { Button, Field, Input, Select } from "../components/UI";

export function Login() {
  const {user,login}=useAuth(), nav=useNavigate();
  const [form,setForm]=useState({username:"admin",password:"password123"});
  const [show,setShow]=useState(false), [err,setErr]=useState(""), [loading,setLoading]=useState(false);
  if(user) return <Navigate to="/" replace/>;
  const submit=async e=>{e.preventDefault();setErr("");setLoading(true);try{await login(form);nav("/")}catch(x){setErr(x.message)}finally{setLoading(false)}};
  return <div className="auth-page"><div className="auth-art"><div className="art-grid"/><div className="art-content"><div className="brand light"><div className="brand-mark"><Sparkles size={18}/></div><div><b>PayFlow</b><small>Payroll OS</small></div></div><div className="hero-copy"><span className="pill">SMART PAYROLL CONTROL</span><h1>Run payroll<br/><em>without the chaos.</em></h1><p>One workspace for people, salary structures, attendance, payroll runs and payslips — connected to your Spring Boot API.</p></div><div className="art-metrics"><div><b>100%</b><span>API driven</span></div><div><b>JWT</b><span>Secured access</span></div><div><b>10+</b><span>Payroll workflows</span></div></div></div></div>
  <div className="auth-panel"><div className="auth-form"><div className="mobile-brand"><Sparkles size={19}/> PayFlow</div><span className="eyebrow">WELCOME BACK</span><h2>Sign in to your workspace</h2><p className="muted">Use the account created by your payroll administrator.</p>
  <form onSubmit={submit}><Field label="Username"><Input autoFocus value={form.username} onChange={e=>setForm({...form,username:e.target.value})} placeholder="admin"/></Field><Field label="Password"><div className="password-wrap"><Input type={show?"text":"password"} value={form.password} onChange={e=>setForm({...form,password:e.target.value})} placeholder="••••••••"/><button type="button" onClick={()=>setShow(!show)}>{show?<EyeOff size={17}/>:<Eye size={17}/>}</button></div></Field>{err&&<div className="error-box">{err}</div>}<Button loading={loading} type="submit" icon={ArrowRight}>Sign in</Button></form>
  <div className="auth-note"><ShieldCheck size={16}/> Protected by JWT authentication</div><p className="auth-switch">Need an account? <Link to="/register">Create one</Link></p></div></div></div>
}

export function Register() {
  const {user,register}=useAuth(),nav=useNavigate();
  const [form,setForm]=useState({username:"",password:"",role:"EMPLOYEE"}),[err,setErr]=useState(""),[loading,setLoading]=useState(false);
  if(user) return <Navigate to="/" replace/>;
  const submit=async e=>{e.preventDefault();setErr("");setLoading(true);try{await register(form);nav("/")}catch(x){setErr(x.message)}finally{setLoading(false)}};
  return <div className="auth-page"><div className="auth-art"><div className="art-grid"/><div className="art-content"><div className="brand light"><div className="brand-mark"><Sparkles size={18}/></div><div><b>PayFlow</b><small>Payroll OS</small></div></div><div className="hero-copy"><span className="pill">PEOPLE → PAYROLL</span><h1>Your payroll<br/><em>starts here.</em></h1><p>Create an authenticated workspace. Every action after sign-in is sent to your backend API and persisted in PostgreSQL.</p></div></div></div>
  <div className="auth-panel"><div className="auth-form"><div className="mobile-brand"><Sparkles size={19}/> PayFlow</div><span className="eyebrow">CREATE ACCOUNT</span><h2>Set up your workspace access</h2><p className="muted">Role controls which payroll actions are available.</p><form onSubmit={submit}><Field label="Username"><Input required value={form.username} onChange={e=>setForm({...form,username:e.target.value})}/></Field><Field label="Password"><Input required minLength={6} type="password" value={form.password} onChange={e=>setForm({...form,password:e.target.value})}/></Field><Field label="Role"><Select value={form.role} onChange={e=>setForm({...form,role:e.target.value})}>{["EMPLOYEE","HR_EXECUTIVE","PAYROLL_ADMIN","FINANCE_MANAGER","SYSTEM_ADMIN"].map(x=><option key={x}>{x}</option>)}</Select></Field>{err&&<div className="error-box">{err}</div>}<Button loading={loading} type="submit" icon={ArrowRight}>Create account</Button></form><p className="auth-switch">Already have access? <Link to="/login">Sign in</Link></p></div></div></div>
}
