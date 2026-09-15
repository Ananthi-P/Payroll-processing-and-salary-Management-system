import { useState } from "react";
import { Check, X, LoaderCircle, Search, ChevronLeft, ChevronRight } from "lucide-react";
import { statusTone } from "../utils";

export function Button({children, variant="primary", icon:Icon, loading=false, ...props}) {
  return <button className={`btn btn-${variant}`} disabled={loading || props.disabled} {...props}>
    {loading ? <LoaderCircle size={16} className="spin"/> : Icon ? <Icon size={16}/> : null}{children}
  </button>
}
export function Card({children, className=""}) { return <section className={`card ${className}`}>{children}</section>; }
export function Field({label, children, hint}) { return <label className="field"><span>{label}</span>{children}{hint && <small>{hint}</small>}</label>; }
export function Input(props) { return <input className="input" {...props}/>; }
export function Select({children,...props}) { return <select className="input" {...props}>{children}</select>; }
export function Modal({title, onClose, children, wide=false}) {
  return <div className="modal-backdrop" onMouseDown={e=>e.target===e.currentTarget&&onClose()}>
    <div className={`modal ${wide?"modal-wide":""}`}>
      <div className="modal-head"><div><h3>{title}</h3></div><button className="icon-btn" onClick={onClose}><X size={18}/></button></div>
      <div className="modal-body">{children}</div>
    </div>
  </div>;
}
export function Badge({children}) { return <span className={`badge badge-${statusTone(children)}`}>{children}</span>; }
export function Empty({title="Nothing here yet", text="Create a record to see it here."}) {
  return <div className="empty"><div className="empty-orb">✦</div><strong>{title}</strong><p>{text}</p></div>;
}
export function Toolbar({value,onChange,placeholder="Search..."}) {
  return <div className="toolbar-search"><Search size={17}/><input value={value} onChange={e=>onChange(e.target.value)} placeholder={placeholder}/></div>;
}
export function Pagination({page,pages,setPage}) {
  if(pages<=1) return null;
  return <div className="pagination"><button className="icon-btn" disabled={page===1} onClick={()=>setPage(page-1)}><ChevronLeft size={16}/></button><span>Page {page} of {pages}</span><button className="icon-btn" disabled={page===pages} onClick={()=>setPage(page+1)}><ChevronRight size={16}/></button></div>
}
export function Confirm({title,text,onYes,onNo}) {
  return <Modal title={title} onClose={onNo}><p className="muted">{text}</p><div className="modal-actions"><Button variant="ghost" onClick={onNo}>Cancel</Button><Button variant="danger" icon={Check} onClick={onYes}>Confirm</Button></div></Modal>
}
