package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Transaction;
import util.JWTUtil;
import util.AuthUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "HistoryServlet", value = "/HistoryServlet")
public class HistoryServlet extends HttpServlet {
    private Transaction transactionModel;
    private Account accountModel;

    @Override
    public void init() throws ServletException {
        transactionModel = new Transaction(); // Khởi tạo Transaction model
        accountModel = new Account(); // Khởi tạo Account model
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy Access Token từ cookie
        String accessToken = AuthUtil.getCookieValue(request, "accessToken");
        if (accessToken == null || !JWTUtil.validateToken(accessToken)) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy username từ token
        String username = JWTUtil.getUsernameFromToken(accessToken);
        int userId;
        try {
            userId = AuthUtil.getUserIdFromUsername(username);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Failed to authenticate user: " + e.getMessage());
            request.getRequestDispatcher("history.jsp").forward(request, response);
            return;
        }

        String accountNumber = request.getParameter("accountNumber");

        // Kiểm tra accountNumber có được gửi không
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            request.setAttribute("error", "No account specified");
            request.getRequestDispatcher("history.jsp").forward(request, response);
            return;
        }

        try {
            // Lấy danh sách tài khoản từ DB thay vì session
            List<Account> accounts = accountModel.getAccountsByUserId(userId);

            // Tìm tài khoản tương ứng với accountNumber
            Account account = accounts.stream()
                    .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                    .findFirst()
                    .orElse(null);

            if (account == null) {
                request.setAttribute("error", "Account not found or not owned by you");
                request.getRequestDispatcher("history.jsp").forward(request, response);
                return;
            }

            // Gọi phương thức để lấy lịch sử giao dịch
            List<Transaction> transactions = transactionModel.getTransactionHistory(account.getAccountNumber());
            request.setAttribute("transactions", transactions);
            request.setAttribute("accountNumber", accountNumber);
            request.getRequestDispatcher("history.jsp").forward(request, response);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while retrieving transaction history: " + e.getMessage());
            request.getRequestDispatcher("history.jsp").forward(request, response);
        }
    }
}