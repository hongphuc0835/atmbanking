package filter; // Đặt package phù hợp với dự án của bạn

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.AuthUtil;
import util.JWTUtil;

import java.io.IOException;

@WebFilter({"/dashboard", "/HistoryServlet", "/DepositServlet", "/WithdrawServlet", "/TransferServlet", "/createAccount"})
public class AuthFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {
            "/login.jsp", "/LoginServlet", "/register.jsp", "/logout" // Các trang công khai
    };


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Cho phép truy cập các trang công khai
        for (String publicPath : PUBLIC_PATHS) {
            if (path.equals(publicPath) || path.startsWith("/resources/")) { // Cho phép tài nguyên tĩnh
                chain.doFilter(request, response);
                return;
            }
        }

        // Kiểm tra Access Token từ cookie
        String accessToken = AuthUtil.getCookieValue(request, "accessToken");
        if (accessToken == null || !JWTUtil.validateToken(accessToken)) {
            System.out.println("Access Token invalid or missing: " + accessToken); // Log để debug
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // Token hợp lệ, tiếp tục xử lý request
        chain.doFilter(request, response);
    }
}