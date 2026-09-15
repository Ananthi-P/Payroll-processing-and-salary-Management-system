const BASE = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api/v1";

async function request(path, options = {}) {
  const token = localStorage.getItem("payflow_token");
  const headers = { "Content-Type": "application/json", ...(options.headers || {}) };
  if (token) headers.Authorization = `Bearer ${token}`;

  const res = await fetch(`${BASE}${path}`, { ...options, headers });
  if (res.status === 204) return null;
  const text = await res.text();
  let data = null;
  try { data = text ? JSON.parse(text) : null; } catch { data = text; }
  if (!res.ok) {
    const msg = data?.message || data?.error || (typeof data === "string" ? data : `Request failed (${res.status})`);
    if (res.status === 401) {
      localStorage.removeItem("payflow_token");
      localStorage.removeItem("payflow_user");
      window.location.href = "/login";
    }
    throw new Error(msg);
  }
  return data;
}

export const api = {
  get: (p) => request(p),
  post: (p, body) => request(p, { method:"POST", body: body === undefined ? undefined : JSON.stringify(body) }),
  put: (p, body) => request(p, { method:"PUT", body: body === undefined ? undefined : JSON.stringify(body) }),
  del: (p) => request(p, { method:"DELETE" }),
  base: BASE
};
