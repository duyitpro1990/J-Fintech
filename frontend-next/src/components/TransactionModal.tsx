"use client";
import { Transaction } from "../types/types";

interface Props {
  transaction: Transaction | null;
  onClose: () => void;
}

export default function TransactionModal({ transaction, onClose }: Props) {
  if (!transaction) return null;

  // Format ngày giờ đẹp
  const dateStr = new Date(transaction.timestamp).toLocaleString("vi-VN");

  // Phân tích nội dung giao dịch để hiển thị đẹp hơn
  let detailText = "Giao dịch hệ thống";
  let relatedAccount = "N/A";

  if (transaction.type.includes("TRANSFER_IN")) {
    detailText = "Nhận tiền chuyển khoản";
    // Mẹo: Trong backend bạn đã lưu chuỗi "From: ID...", ở đây có thể parse ra nếu muốn
    relatedAccount = transaction.type.split("From: ")[1]?.replace(")", "") || "Ẩn danh";
  } else if (transaction.type.includes("TRANSFER_OUT")) {
    detailText = "Chuyển tiền đi";
    relatedAccount = transaction.type.split("To: ")[1]?.replace(")", "") || "Ẩn danh";
  } else if (transaction.type === "DEPOSIT") {
    detailText = "Nạp tiền tại quầy/ATM";
    relatedAccount = "Hệ thống J-Fintech";
  } else if (transaction.type === "WITHDRAW") {
    detailText = "Rút tiền mặt";
    relatedAccount = "Hệ thống J-Fintech";
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-2xl w-full max-w-md overflow-hidden animate-fade-in-up">
        {/* Header */}
        <div className="bg-blue-600 p-4 text-white flex justify-between items-center">
          <h3 className="text-lg font-bold">🧾 Chi tiết giao dịch #{transaction.id}</h3>
          <button onClick={onClose} className="text-2xl font-bold hover:text-gray-200">&times;</button>
        </div>

        {/* Body */}
        <div className="p-6 space-y-4">
          <div className="flex justify-between border-b pb-2">
            <span className="text-gray-500">Thời gian</span>
            <span className="font-medium">{dateStr}</span>
          </div>
          <div className="flex justify-between border-b pb-2">
            <span className="text-gray-500">Loại giao dịch</span>
            <span className="font-medium text-blue-600">{detailText}</span>
          </div>
          <div className="flex justify-between border-b pb-2">
            <span className="text-gray-500">Số tiền</span>
            <span className={`font-bold text-xl ${transaction.type.includes("DEPOSIT") || transaction.type.includes("IN") ? "text-green-500" : "text-red-500"}`}>
              {new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(transaction.amount)}
            </span>
          </div>
          <div className="flex justify-between border-b pb-2">
            <span className="text-gray-500">Đối tác (ID)</span>
            <span className="font-medium">{relatedAccount}</span>
          </div>
          
          <div className="mt-4 p-3 bg-gray-50 rounded text-sm text-gray-500 text-center">
            Mã tham chiếu hệ thống: {transaction.timestamp.replace(/[-:.]/g, "")}{transaction.id}
          </div>
        </div>

        {/* Footer */}
        <div className="p-4 bg-gray-100 text-center">
          <button onClick={onClose} className="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg transition">
            Đóng
          </button>
        </div>
      </div>
    </div>
  );
}