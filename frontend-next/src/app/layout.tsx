import type { Metadata } from "next";
import "./globals.css";
import { AuthProvider } from "@/src/context/AuthContext"; // Đường dẫn tuyệt đối đến AuthContext

export const metadata: Metadata = {
  title: "J-Fintech App",
  description: "Internet Banking Application",
};

export default function RootLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="vi">
      <body>
        <AuthProvider>
          {children}
        </AuthProvider>
      </body>
    </html>
  );
}