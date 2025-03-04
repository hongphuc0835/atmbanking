package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.User;
import util.JWTUtil;
import util.AuthUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/dashboard")
public class LoadDashboardServlet extends HttpServlet {
    private Account accountModel;

    @Override
    public void init() throws ServletException {
        accountModel = new Account();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accessToken = AuthUtil.getCookieValue(request, "accessToken");
        String username = JWTUtil.getUsernameFromToken(accessToken);
        int userId;
        try {
            userId = AuthUtil.getUserIdFromUsername(username);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Không thể xác thực người dùng: " + e.getMessage());
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        try {
            List<Account> accounts = accountModel.getAccountsByUserId(userId);
            User user = new User();
            user.setUserId(userId);
            user.setUsername(username);
            request.setAttribute("accounts", accounts);
            request.setAttribute("user", user);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Không thể tải danh sách tài khoản: " + e.getMessage());
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }
}