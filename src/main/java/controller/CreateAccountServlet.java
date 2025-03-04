package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import util.DBUtil;
import util.JWTUtil;
import util.AuthUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@WebServlet("/createAccount")
public class CreateAccountServlet extends HttpServlet {
    private Account accountModel;

    @Override
    public void init() throws ServletException {
        accountModel = new Account();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // AuthFilter đã kiểm tra token, chỉ cần lấy thông tin user từ token
        String accessToken = AuthUtil.getCookieValue(request, "accessToken");
        String username = JWTUtil.getUsernameFromToken(accessToken);
        int userId;
        try {
            userId = AuthUtil.getUserIdFromUsername(username);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Failed to authenticate user: " + e.getMessage());
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        String accountNumber = generateRandomAccountNumber();
        double balance = 0.0;

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setUserId(userId);
        account.setBalance(balance);

        try {
            saveAccountToDatabase(account);
            List<Account> accounts = accountModel.getAccountsByUserId(userId);
            request.setAttribute("accounts", accounts);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Failed to create account: " + e.getMessage());
            request.getRequestDispatcher("dashboard").forward(request, response);
        }
    }

    private String generateRandomAccountNumber() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    private void saveAccountToDatabase(Account account) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO accounts (account_number, user_id, balance) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, account.getAccountNumber());
            stmt.setInt(2, account.getUserId());
            stmt.setDouble(3, account.getBalance());
            stmt.executeUpdate();
        }
    }
}