package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.Account;
import util.DBUtil;
import util.JWTUtil;
import util.AuthUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    private Account accountModel;

    @Override
    public void init() throws ServletException {
        accountModel = new Account();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT user_id, username, password FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));

                    String accessToken = JWTUtil.generateAccessToken(username);
                    String refreshToken = JWTUtil.generateRefreshToken(username);

                    Cookie accessCookie = new Cookie("accessToken", accessToken);
                    accessCookie.setHttpOnly(true);
                    accessCookie.setSecure(true);
                    accessCookie.setPath("/");
                    accessCookie.setMaxAge(15 * 60);
                    response.addCookie(accessCookie);

                    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
                    refreshCookie.setHttpOnly(true);
                    refreshCookie.setSecure(true);
                    refreshCookie.setPath("/");
                    refreshCookie.setMaxAge(7 * 24 * 60 * 60);
                    response.addCookie(refreshCookie);

                    response.sendRedirect("dashboard"); // Chuyển hướng tới LoadDashboardServlet
                    return;
                }
            }
            request.setAttribute("error", "Invalid credentials");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during login");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}