"use client";
import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import { useRouter } from "next/navigation";

interface AuthContextType {
  token: string | null;
  login: (token: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(null);
  const router = useRouter();

  // Khi mở web, kiểm tra xem có lưu đăng nhập cũ không
  useEffect(() => {
    const savedToken = localStorage.getItem("jfintech_auth");
    if (savedToken) {
        setToken(savedToken);
    }
  }, []);

  const login = (newToken: string) => {
    setToken(newToken);
    localStorage.setItem("jfintech_auth", newToken);
    router.push("/"); // Chuyển hướng về trang chủ
  };

  const logout = () => {
    setToken(null);
    localStorage.removeItem("jfintech_auth");
    router.push("/login"); // Chuyển hướng về login
  };

  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// Hook để dùng nhanh ở các trang khác
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
}