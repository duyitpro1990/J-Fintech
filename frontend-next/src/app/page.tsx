"use client";
import { useEffect, useState } from "react";
import { useAuth } from "@/src/context/AuthContext"; // Dùng context
import { useRouter } from "next/navigation";
import { BankAccount, Transaction } from "@/src/types/types";
import TransactionModal from "@/src/components/TransactionModal";

const API_BASE = "http://localhost:8080";

export default function Dashboard() {
  const { token, logout } = useAuth();
  const router = useRouter();
  
  // States
  const [accounts, setAccounts] = useState<BankAccount[]>([]);
  const [selectedAccIdx, setSelectedAccIdx] = useState(0);
  const [amount, setAmount] = useState("");
  const [toAccountNumber, setToAccountNumber] = useState("");
  const [selectedTx, setSelectedTx] = useState<Transaction | null>(null);

  // Kiểm tra đăng nhập
  useEffect(() => {
    if (!token) {
      router.push("/login"); // Chưa đăng nhập thì đá về login
    } else {
      fetchData();
    }
  }, [token]);

  const fetchData = async () => {
    if (!token) return;
    try {
      const res = await fetch(`${API_BASE}/accounts`, {
        headers: { Authorization: token },
      });
      if (res.ok) {
        const data = await res.json();
        setAccounts(data);
      }
    } catch (e) { console.error(e); }
  };

  const createAccount = async () => {
    if (!token) return;
    await fetch(`${API_BASE}/accounts`, {
      method: "POST",
      headers: { Authorization: token, "Content-Type": "application/json" },
      body: JSON.stringify({ balance: 0 }),
    });
    fetchData(); // Load lại để thấy tài khoản mới
  };

  // Logic giao dịch (Nạp/Rút/Chuyển)
  const handleTransaction = async (type: "deposit" | "withdraw" | "transfer") => {
    if (!token || !accounts[selectedAccIdx]) return;
    const currentAcc = accounts[selectedAccIdx];

    try {
      let url = "";
      let method = "POST"; 

      if (type === "transfer") {
        if(!toAccountNumber) return alert("Vui lòng nhập số tài khoản người nhận!");
        url = `${API_BASE}/transfer?fromId=${currentAcc.id}&toAccountNumber=${toAccountNumber}&amount=${amount}`;
      } else {
        method = "PUT";
        url = `${API_BASE}/accounts/${currentAcc.id}/${type}?amount=${amount}`;
      }

      const res = await fetch(url, { method, headers: { Authorization: token } });

      if (res.ok) {
        alert("Giao dịch thành công!");
        setAmount("");
        setToAccountNumber("");
        fetchData();
      } else {
        const err = await res.json();
        alert("Lỗi: " + err.message);
      }
    } catch (e) { alert("Lỗi hệ thống"); }
  };

  const formatMoney = (num: number) =>
    new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(num);

  if (!token || accounts.length === 0) return null; // Hoặc hiện loading spinner

  const currentAcc = accounts[selectedAccIdx];

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex justify-center font-sans">
      <div className="w-full max-w-2xl space-y-6">
        
        {/* Header Dashboard */}
        <div className="bg-white p-6 rounded-2xl shadow-lg border border-gray-200">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800 flex items-center gap-2">
              👋 Xin chào, <span className="text-blue-600">{currentAcc?.ownerName}</span>
            </h2>
            <button 
              onClick={logout} 
              className="text-red-600 text-sm font-bold hover:bg-red-50 px-4 py-2 rounded-lg transition"
            >
              Đăng xuất
            </button>
          </div>

          <div className="mb-6">
             <label className="text-sm font-bold text-gray-700 mb-2 block uppercase tracking-wide">
               Tài khoản nguồn:
             </label>
             <select 
               className="w-full p-3 border border-gray-300 rounded-xl bg-white text-gray-900 font-medium focus:ring-2 focus:ring-blue-500 outline-none shadow-sm"
               onChange={(e) => setSelectedAccIdx(Number(e.target.value))}
               value={selectedAccIdx}
             >
               {accounts.map((acc, idx) => (
                 <option key={acc.id} value={idx} className="text-gray-900">
                    STK: {acc.accountNumber} - {formatMoney(acc.balance)}
                 </option>
               ))}
             </select>
          </div>

          {/* Card Số dư */}
          <div className="bg-gradient-to-br from-blue-600 to-blue-700 p-6 rounded-2xl text-white text-center mb-8 shadow-xl relative overflow-hidden">
            <div className="relative z-10">
              <div className="text-blue-100 font-medium mb-1">Số dư khả dụng</div>
              <div className="text-5xl font-extrabold tracking-tight my-2">
                {formatMoney(currentAcc.balance)}
              </div>
            </div>
            {/* Hiệu ứng trang trí nền */}
            <div className="absolute top-0 right-0 -mr-4 -mt-4 w-24 h-24 rounded-full bg-white opacity-10 blur-xl"></div>
            <div className="absolute bottom-0 left-0 -ml-4 -mb-4 w-20 h-20 rounded-full bg-white opacity-10 blur-xl"></div>
          </div>

          {/* Form thực hiện giao dịch */}
          <div className="space-y-4">
             {/* Input ID Người nhận */}
             <div className="relative">
               <input
                type="number"
                min={0}
                placeholder="Nhập số tài khoản người nhận (chỉ dùng khi chuyển khoản)"
                className="w-full p-4 border border-gray-300 rounded-xl text-gray-900 placeholder-gray-500 focus:ring-2 focus:ring-blue-500 outline-none shadow-sm font-medium"
                value={toAccountNumber}
                onChange={(e) => setToAccountNumber(e.target.value)}
              />
            </div>

            {/* Input Số tiền */}
            <div className="relative">
              <input
                type="number"
                placeholder="Nhập số tiền giao dịch..."
                className="w-full p-4 border border-gray-300 rounded-xl text-gray-900 placeholder-gray-500 focus:ring-2 focus:ring-blue-500 outline-none font-bold text-lg shadow-sm"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
              />
              <span className="absolute right-10 top-5 text-gray-400 font-bold">VND</span>
            </div>

            {/* Nút bấm */}
            <div className="grid grid-cols-3 gap-3 pt-2">
              <button 
                onClick={() => handleTransaction("transfer")} 
                className="bg-purple-600 text-white py-3.5 rounded-xl font-bold hover:bg-purple-700 shadow-md active:scale-95 transition"
              >
                🚀 Chuyển khoản
              </button>
              <button 
                onClick={() => handleTransaction("deposit")} 
                className="bg-green-500 text-white py-3.5 rounded-xl font-bold hover:bg-green-600 shadow-md active:scale-95 transition"
              >
                📥 Nạp tiền
              </button>
              <button 
                onClick={() => handleTransaction("withdraw")} 
                className="bg-red-500 text-white py-3.5 rounded-xl font-bold hover:bg-red-600 shadow-md active:scale-95 transition"
              >
                📤 Rút tiền
              </button>
            </div>
             <div className="text-right mt-2">
                <button 
                  onClick={createAccount} 
                  className="text-sm text-blue-600 hover:text-blue-800 font-semibold hover:underline transition"
                >
                  + Mở thêm tài khoản phụ
                </button>
            </div>
          </div>
        </div>

        {/* Lịch sử giao dịch */}
        <div className="bg-white p-6 rounded-2xl shadow-lg border border-gray-200">
          <h3 className="text-xl font-bold text-gray-800 mb-4 border-b pb-4 flex items-center gap-2">
            📜 Lịch sử giao dịch
          </h3>
          <div className="max-h-96 overflow-y-auto pr-2 custom-scrollbar space-y-3">
            {currentAcc?.transactions?.length === 0 && (
                <div className="text-center py-10">
                  <p className="text-gray-400 font-medium">Chưa có giao dịch nào phát sinh.</p>
                </div>
            )}
            {currentAcc?.transactions
              ?.sort((a, b) => b.id - a.id)
              .map((tx) => {
                const isPlus = tx.type.includes("DEPOSIT") || tx.type.includes("IN");
                return (
                  <div 
                    key={tx.id} 
                    onClick={() => setSelectedTx(tx)}
                    className="flex justify-between items-center p-4 bg-gray-50 hover:bg-blue-50 cursor-pointer transition rounded-xl border border-gray-100 group"
                  >
                    <div>
                      <div className="font-bold text-gray-800 group-hover:text-blue-700 transition">
                        {tx.type}
                      </div>
                      <div className="text-xs text-gray-500 mt-1 font-medium">
                        {new Date(tx.timestamp).toLocaleString("vi-VN")}
                      </div>
                    </div>
                    <div className={`font-extrabold text-lg ${isPlus ? "text-green-600" : "text-red-500"}`}>
                      {isPlus ? "+" : ""} {formatMoney(tx.amount)}
                    </div>
                  </div>
                );
              })}
          </div>
        </div>
      </div>

      {/* MODAL */}
      <TransactionModal transaction={selectedTx} onClose={() => setSelectedTx(null)} />
    </div>
  );
}