package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import util.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@WebServlet("/createAccount")
public class CreateAccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy userId từ request
        String userIdStr = request.getParameter("userId");

        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            request.setAttribute("error", "User ID is required.");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format.");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        // Sinh accountNumber ngẫu nhiên 6 chữ số
        String accountNumber = generateRandomAccountNumber();
        double balance = 0.0; // Số dư mặc định

        // Tạo tài khoản mới
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setUserId(userId);
        account.setBalance(balance);

        try {
            // Lưu tài khoản vào cơ sở dữ liệu
            saveAccountToDatabase(account);

            // Lấy danh sách tài khoản cập nhật của người dùng
            Account accountModel = new Account();
            List<Account> accounts = accountModel.getAccountsByUserId(userId);

            // Đặt dữ liệu vào request để hiển thị trên dashboard.jsp
            request.setAttribute("accounts", accounts);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Failed to create account: " + e.getMessage());
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }

    private String generateRandomAccountNumber() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // Sinh số ngẫu nhiên từ 100000 đến 999999
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported.");
    }
}