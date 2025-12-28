"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function RegisterPage() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPass, setConfirmPass] = useState("");
  const [loading, setLoading] = useState(false);

  const handleRegister = async () => {
    if (!username || !password) return alert("Nhập thiếu thông tin!");
    if (password !== confirmPass) return alert("Mật khẩu xác nhận không khớp!");
    
    setLoading(true);
    try {
      const res = await fetch(`http://localhost:8080/register?username=${username}&password=${password}`, {
        method: "POST",
      });

      if (res.ok) {
        alert("Đăng ký thành công! Vui lòng đăng nhập.");
        router.push("/login");
      } else {
        alert("Tên đăng nhập đã tồn tại hoặc lỗi hệ thống.");
      }
    } catch (e) {
      alert("Lỗi kết nối Server!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 font-sans">
      <div className="bg-white p-8 rounded-2xl shadow-xl w-96 border border-gray-200">
        <h1 className="text-3xl font-extrabold text-center text-green-600 mb-2">Tạo tài khoản</h1>
        <p className="text-center text-gray-500 font-medium mb-8">Gia nhập J-Fintech ngay</p>

        <div className="space-y-4">
          <div>
            <label className="block text-sm font-bold text-gray-700 mb-1">Tên đăng nhập mới</label>
            <input
              className="w-full p-3.5 border border-gray-300 rounded-xl text-gray-900 placeholder-gray-500 focus:ring-2 focus:ring-green-500 outline-none font-medium transition"
              placeholder="Chọn username..."
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>

          <div>
            <label className="block text-sm font-bold text-gray-700 mb-1">Mật khẩu</label>
            <input
              className="w-full p-3.5 border border-gray-300 rounded-xl text-gray-900 placeholder-gray-500 focus:ring-2 focus:ring-green-500 outline-none font-medium transition"
              type="password"
              placeholder="Nhập password..."
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <div>
            <label className="block text-sm font-bold text-gray-700 mb-1">Nhập lại mật khẩu</label>
            <input
              className="w-full p-3.5 border border-gray-300 rounded-xl text-gray-900 placeholder-gray-500 focus:ring-2 focus:ring-green-500 outline-none font-medium transition"
              type="password"
              placeholder="Xác nhận password..."
              value={confirmPass}
              onChange={(e) => setConfirmPass(e.target.value)}
            />
          </div>
        </div>

        <button
          onClick={handleRegister}
          disabled={loading}
          className="w-full bg-green-600 text-white p-3.5 rounded-xl font-bold text-lg mt-8 hover:bg-green-700 active:scale-95 transition shadow-md disabled:bg-gray-400"
        >
          {loading ? "Đang xử lý..." : "Đăng ký"}
        </button>

        <div className="text-center mt-6 text-sm font-medium">
          <Link href="/login" className="text-gray-500 hover:text-gray-900 hover:underline transition">
            ← Quay lại Đăng nhập
          </Link>
        </div>
      </div>
    </div>
  );
}