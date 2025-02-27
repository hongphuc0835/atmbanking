package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TransferServlet", value = "/TransferServlet")
public class TransferServlet extends HttpServlet {
    private Account accountModel;

    @Override
    public void init() throws ServletException {
        accountModel = new Account(); // Khởi tạo AccountModel
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId"); // Lấy userId từ session
        String accountNumber = request.getParameter("accountNumber"); // Lấy accountNumber từ form (tài khoản nguồn)
        String targetAccount = request.getParameter("targetAccount"); // Tài khoản đích
        double amount;

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Kiểm tra và parse số tiền
        try {
            amount = Double.parseDouble(request.getParameter("amount"));
            if (amount <= 0) {
                request.setAttribute("error", "Amount must be greater than 0");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        try {
            // Lấy danh sách tài khoản từ session
            List<Account> accounts = (List<Account>) session.getAttribute("accounts");
            if (accounts == null) {
                response.sendRedirect("DashboardServlet"); // Nếu không có danh sách, quay lại dashboard
                return;
            }

            // Tìm tài khoản nguồn tương ứng với accountNumber
            Account account = accounts.stream()
                    .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                    .findFirst()
                    .orElse(null);

            if (account == null) {
                request.setAttribute("error", "Source account not found or not owned by you");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }

            // Kiểm tra tài khoản nhận
            if (!accountModel.checkTargetAccountExists(targetAccount)) {
                request.setAttribute("error", "Target account does not exist");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }

            // Kiểm tra số dư
            if (!accountModel.hasSufficientBalance(account, amount)) {
                request.setAttribute("error", "Insufficient balance");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }

            // Thực hiện chuyển tiền
            accountModel.transfer(account, targetAccount, amount);

            // Cập nhật lại danh sách tài khoản trong session
            session.setAttribute("accounts", accountModel.getAccountsByUserId(userId));
            response.sendRedirect("dashboard.jsp"); // Chuyển hướng đến Dashboard

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during the transfer");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
        }
    }
}