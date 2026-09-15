export const money = (v) => `₹${Number(v || 0).toLocaleString("en-IN", {maximumFractionDigits: 0})}`;
export const monthName = (m) => new Date(2000, Number(m)-1, 1).toLocaleString("en-IN", {month:"short"});
export const today = () => new Date().toISOString().slice(0,10);
export const cls = (...x) => x.filter(Boolean).join(" ");
export const can = (role, roles) => roles.includes(role);
export const statusTone = (status="") => {
  const s=status.toUpperCase();
  if(["APPROVED","PAID","DISBURSED","ACTIVE","COMPUTED","PRESENT"].includes(s)) return "success";
  if(["PENDING","DRAFT","ON_LEAVE"].includes(s)) return "warning";
  if(["REJECTED","TERMINATED","ABSENT"].includes(s)) return "danger";
  return "info";
};
