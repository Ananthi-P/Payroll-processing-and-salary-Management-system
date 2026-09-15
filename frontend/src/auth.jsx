import { createContext, useContext, useEffect, useState } from "react";
import { api } from "./api";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem("payflow_user")) || null; } catch { return null; }
  });

  useEffect(() => {
    if (user) localStorage.setItem("payflow_user", JSON.stringify(user));
    else localStorage.removeItem("payflow_user");
  }, [user]);

  const login = async (payload) => {
    const data = await api.post("/auth/login", payload);
    localStorage.setItem("payflow_token", data.token);
    setUser(data);
    return data;
  };

  const register = async (payload) => {
    const data = await api.post("/auth/register", payload);
    localStorage.setItem("payflow_token", data.token);
    setUser(data);
    return data;
  };

  const logout = () => {
    localStorage.removeItem("payflow_token");
    setUser(null);
    window.location.href = "/login";
  };

  return <AuthContext.Provider value={{user, login, register, logout}}>
    {children}
  </AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);
