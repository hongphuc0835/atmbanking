package util;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JWTUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long ACCESS_TOKEN_EXPIRY = 15 * 60 * 1000; // 15 phút
    private static final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60 * 1000; // 7 ngày

    // Tạo Access Token
    public static String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Lưu thông tin người dùng
                .setIssuedAt(new Date()) // Thời điểm phát hành
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY)) // Hết hạn sau 15 phút
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // Ký bằng SECRET_KEY
                .compact();
    }

    // Tạo Refresh Token
    public static String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY)) // Hết hạn sau 7 ngày
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Giải mã JWT
    public static Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Kiểm tra token hợp lệ
    public static boolean validateToken(String token) {
        try {
            decodeToken(token);
            return true; // Token hợp lệ
        } catch (Exception e) {
            return false; // Token không hợp lệ (hết hạn, chữ ký sai, v.v.)
        }
    }

    // Lấy username từ token
    public static String getUsernameFromToken(String token) {
        Claims claims = decodeToken(token);
        return claims.getSubject();
    }
}