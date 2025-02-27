package model;

import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private int id;
    private String accountNumber;
    private String type;
    private double amount;
    private String targetAccount;
    private Timestamp transactionDate;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getTargetAccount() { return targetAccount; }
    public void setTargetAccount(String targetAccount) { this.targetAccount = targetAccount; }
    public Timestamp getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Timestamp transactionDate) { this.transactionDate = transactionDate; }


    // Lấy lịch sử giao dịch (mới)
    public List<Transaction> getTransactionHistory(String accountNumber) throws SQLException, ClassNotFoundException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction tx = new Transaction();
                tx.setId(rs.getInt("id"));
                tx.setAccountNumber(rs.getString("account_number"));
                tx.setType(rs.getString("type"));
                tx.setAmount(rs.getDouble("amount"));
                tx.setTargetAccount(rs.getString("target_account"));
                tx.setTransactionDate(rs.getTimestamp("transaction_date"));
                transactions.add(tx);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve transaction history: " + e.getMessage());
        }
        return transactions;
    }
}