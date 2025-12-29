import type { Metadata } from "next";
import "./globals.css";
import { AuthProvider } from "@/src/context/AuthContext"; // Đường dẫn tuyệt đối đến AuthContext
import { Toaster } from "react-hot-toast";

export const metadata: Metadata = {
  title: "J-Fintech App",
  description: "Internet Banking Application",
};

export default function RootLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="vi">
      <body suppressContentEditableWarning={true}>
        <AuthProvider>
          {children}
          <Toaster position="top-center" /> {/* Thêm Toaster ở đây để có thể dùng ở mọi trang */}
        </AuthProvider>
      </body>
    </html>
  );
}