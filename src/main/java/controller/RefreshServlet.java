package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JWTUtil;
import util.AuthUtil;

import java.io.IOException;

@WebServlet("/refresh")
public class RefreshServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String refreshToken = AuthUtil.getCookieValue(request, "refreshToken");
        if (refreshToken == null || !JWTUtil.validateToken(refreshToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired refresh token");
            return;
        }

        String username = JWTUtil.getUsernameFromToken(refreshToken);
        String newAccessToken = JWTUtil.generateAccessToken(username);

        Cookie accessCookie = new Cookie("accessToken", newAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60);
        response.addCookie(accessCookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}