package model;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Account {
    private String accountNumber;
    private int userId;
    private double balance;

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }


    private static final double MAX_WITHDRAW = 5000;
    // Kiểm tra tài khoản nhận có tồn tại không
    public boolean checkTargetAccountExists(String targetAccount) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBUtil.getConnection()) {
            String checkSql = "SELECT * FROM accounts WHERE account_number = ?";
            PreparedStatement stmt = conn.prepareStatement(checkSql);
            stmt.setString(1, targetAccount);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // Kiểm tra số dư
    public boolean hasSufficientBalance(Account account, double amount) {
        return amount <= account.getBalance();
    }

    // Thực hiện chuyển tiền
    public void transfer(Account account, String targetAccount, double amount) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Cập nhật số dư người gửi
            String senderSql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            PreparedStatement stmt = conn.prepareStatement(senderSql);
            stmt.setDouble(1, amount);
            stmt.setString(2, account.getAccountNumber());
            stmt.executeUpdate();

            // Cập nhật số dư người nhận
            String receiverSql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            stmt = conn.prepareStatement(receiverSql);
            stmt.setDouble(1, amount);
            stmt.setString(2, targetAccount);
            stmt.executeUpdate();

            // Ghi lại giao dịch
            String insertSql = "INSERT INTO transactions (account_number, type, amount, target_account) VALUES (?, 'Transfer', ?, ?)";
            stmt = conn.prepareStatement(insertSql);
            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, amount);
            stmt.setString(3, targetAccount);
            stmt.executeUpdate();

            conn.commit(); // Commit transaction
            account.setBalance(account.getBalance() - amount); // Cập nhật số dư trong đối tượng Account
        } catch (SQLException e) {
            throw new SQLException("Transfer failed: " + e.getMessage());
        }
    }

    // Kiểm tra điều kiện rút tiền (mới)
    public boolean canWithdraw(Account account, double amount) {
        return amount <= MAX_WITHDRAW && amount <= account.getBalance();
    }

    // Thực hiện rút tiền (mới)
    public void withdraw(Account account, double amount) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            // Cập nhật số dư
            String updateSql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt.setDouble(1, amount);
            stmt.setString(2, account.getAccountNumber());
            stmt.executeUpdate();

            // Ghi lại giao dịch
            String insertSql = "INSERT INTO transactions (account_number, type, amount) VALUES (?, 'Withdraw', ?)";
            stmt = conn.prepareStatement(insertSql);
            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, amount);
            stmt.executeUpdate();

            conn.commit();
            account.setBalance(account.getBalance() - amount); // Cập nhật số dư trong đối tượng Account
        } catch (SQLException e) {
            throw new SQLException("Withdraw failed: " + e.getMessage());
        }
    }

    // Thực hiện nạp tiền (mới)
    public void deposit(Account account, double amount) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            // Cập nhật số dư
            String updateSql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt.setDouble(1, amount);
            stmt.setString(2, account.getAccountNumber());
            stmt.executeUpdate();

            // Ghi lại giao dịch
            String insertSql = "INSERT INTO transactions (account_number, type, amount) VALUES (?, 'Deposit', ?)";
            stmt = conn.prepareStatement(insertSql);
            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, amount);
            stmt.executeUpdate();

            conn.commit();
            account.setBalance(account.getBalance() + amount); // Cập nhật số dư trong đối tượng Account
        } catch (SQLException e) {
            throw new SQLException("Deposit failed: " + e.getMessage());
        }
    }

    public List<Account> getAccountsByUserId(int userId) throws SQLException, ClassNotFoundException {
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT account_number, user_id, balance FROM accounts WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Account account = new Account();
                account.setAccountNumber(rs.getString("account_number"));
                account.setUserId(rs.getInt("user_id"));
                account.setBalance(rs.getDouble("balance"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve accounts for user: " + e.getMessage());
        }
        return accounts;
    }
}