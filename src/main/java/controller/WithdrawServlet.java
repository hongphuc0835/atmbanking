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

@WebServlet(name = "WithdrawServlet", value = "/WithdrawServlet")
public class WithdrawServlet extends HttpServlet {
    private Account accountModel;

    @Override
    public void init() throws ServletException {
        accountModel = new Account();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accessToken = AuthUtil.getCookieValue(request, "accessToken");
        if (accessToken == null || !JWTUtil.validateToken(accessToken)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = JWTUtil.getUsernameFromToken(accessToken);
        int userId;
        try {
            userId = AuthUtil.getUserIdFromUsername(username);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Failed to authenticate user: " + e.getMessage());
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
            return;
        }

        String accountNumber = request.getParameter("accountNumber");
        double amount;

        try {
            amount = Double.parseDouble(request.getParameter("amount"));
            if (amount <= 0) {
                request.setAttribute("error", "Amount must be greater than 0");
                request.getRequestDispatcher("withdraw.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
            return;
        }

        try {
            List<Account> accounts = accountModel.getAccountsByUserId(userId);
            Account account = accounts.stream()
                    .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                    .findFirst()
                    .orElse(null);

            if (account == null) {
                request.setAttribute("error", "Account not found or not owned by you");
                request.getRequestDispatcher("withdraw.jsp").forward(request, response);
                return;
            }

            if (!accountModel.canWithdraw(account, amount)) {
                request.setAttribute("error", "Invalid amount or insufficient balance");
                request.getRequestDispatcher("withdraw.jsp").forward(request, response);
                return;
            }

            accountModel.withdraw(account, amount);
            response.sendRedirect("dashboard");
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("error", "An error occurred during withdrawal: " + e.getMessage());
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
        }
    }
}