package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Transaction;

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
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId"); // Lấy userId từ session
        String accountNumber = request.getParameter("accountNumber"); // Lấy accountNumber từ yêu cầu

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Kiểm tra accountNumber có được gửi không
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            request.setAttribute("error", "No account specified");
            request.getRequestDispatcher("history.jsp").forward(request, response);
            return;
        }

        try {
            // Lấy danh sách tài khoản từ session
            List<Account> accounts = (List<Account>) session.getAttribute("accounts");
            if (accounts == null) {
                response.sendRedirect("dashboard.jsp"); // Nếu không có danh sách, quay lại dashboard
                return;
            }

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
            request.setAttribute("accountNumber", accountNumber); // Gửi accountNumber để hiển thị trên JSP
            request.getRequestDispatcher("history.jsp").forward(request, response);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while retrieving transaction history");
            request.getRequestDispatcher("history.jsp").forward(request, response);
        }
    }
}