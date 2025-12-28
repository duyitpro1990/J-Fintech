"use client";
import { useState } from "react";
import { useAuth } from "@/src/context/AuthContext";
import Link from "next/link"; // Dùng Link để chuyển trang mượt mà

export default function LoginPage() {
  const { login } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async () => {
    if (!username || !password) return alert("Vui lòng nhập đủ thông tin!");
    setLoading(true);

    const authString = "Basic " + btoa(`${username}:${password}`);
    
    try {
      // Gọi thử API để kiểm tra pass đúng không
      const res = await fetch("http://localhost:8080/accounts", {
        headers: { Authorization: authString },
      });

      if (res.ok) {
        login(authString); // Lưu token và tự động chuyển về trang chủ
      } else {
        alert("Sai tên đăng nhập hoặc mật khẩu!");
      }
    } catch (error) {
      alert("Lỗi kết nối Server!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 font-sans">
      <div className="bg-white p-8 rounded-2xl shadow-xl w-96 border border-gray-200">
        <h1 className="text-3xl font-extrabold text-center text-blue-600 mb-2">J-Fintech</h1>
        <p className="text-center text-gray-500 font-medium mb-8">Đăng nhập hệ thống</p>
        
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-bold text-gray-700 mb-1">Tên đăng nhập</label>
            <input
              className="w-full p-3.5 border border-gray-300 rounded-xl text-gray-900 placeholder-gray-500 focus:ring-2 focus:ring-blue-500 outline-none font-medium transition"
              placeholder="Nhập username..."
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          
          <div>
            <label className="block text-sm font-bold text-gray-700 mb-1">Mật khẩu</label>
            <input
              className="w-full p-3.5 border border-gray-300 rounded-xl text-gray-900 placeholder-gray-500 focus:ring-2 focus:ring-blue-500 outline-none font-medium transition"
              type="password"
              placeholder="Nhập password..."
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && handleLogin()}
            />
          </div>
        </div>
        
        <button
          onClick={handleLogin}
          disabled={loading}
          className="w-full bg-blue-600 text-white p-3.5 rounded-xl font-bold text-lg mt-8 hover:bg-blue-700 active:scale-95 transition disabled:bg-gray-400 shadow-md"
        >
          {loading ? "Đang xử lý..." : "Đăng nhập"}
        </button>

        <div className="text-center mt-6 text-sm font-medium text-gray-600">
          Chưa có tài khoản?{" "}
          <Link href="/register" className="text-blue-600 hover:text-blue-800 hover:underline font-bold transition">
            Đăng ký ngay
          </Link>
        </div>
      </div>
    </div>
  );
}