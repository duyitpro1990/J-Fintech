export interface Transaction {
  id: number;
  amount: number;
  type: string;
  timestamp: string;
}

export interface BankAccount {
  id: number;
  accountNumber: string; // Số TK đẹp
  ownerName: string;
  balance: number;
  transactions: Transaction[];
}