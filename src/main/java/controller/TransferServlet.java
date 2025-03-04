package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import util.JWTUtil;
import util.AuthUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TransferServlet", value = "/TransferServlet")
public class TransferServlet extends HttpServlet {
    private Account accountModel;

    @Override
    public void init() throws ServletException {
        accountModel = new Account();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accessToken = AuthUtil.getCookieValue(request, "accessToken");
        String username = JWTUtil.getUsernameFromToken(accessToken);
        int userId;
        try {
            userId = AuthUtil.getUserIdFromUsername(username);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Không thể xác thực người dùng: " + e.getMessage());
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        String accountNumber = request.getParameter("accountNumber");
        String targetAccount = request.getParameter("targetAccount");
        double amount;

        try {
            amount = Double.parseDouble(request.getParameter("amount"));
            if (amount <= 0) {
                request.setAttribute("error", "Số tiền phải lớn hơn 0");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Định dạng số tiền không hợp lệ");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        try {
            List<Account> accounts = accountModel.getAccountsByUserId(userId);
            Account account = accounts.stream()
                    .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                    .findFirst()
                    .orElse(null);

            if (account == null) {
                request.setAttribute("error", "Tài khoản nguồn không tồn tại hoặc không thuộc về bạn");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }

            if (!accountModel.checkTargetAccountExists(targetAccount)) {
                request.setAttribute("error", "Tài khoản đích không tồn tại");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }

            if (!accountModel.hasSufficientBalance(account, amount)) {
                request.setAttribute("error", "Số dư không đủ");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }

            accountModel.transfer(account, targetAccount, amount);
            response.sendRedirect("dashboard");
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình chuyển khoản: " + e.getMessage());
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
        }
    }
}