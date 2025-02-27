//package util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import org.eclipse.tags.shaded.org.apache.bcel.generic.PUSH;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//
//public class JWTUtil extends HttpServlet {
//   private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);;
//
//   //tao JWT
//    public static String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)  // luu thong tin nguoi dung
//                .setIssuedAt(new Date())   //thoi diem phat hanh
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // het han sau 1 ngay
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // ky bang thuat toan HS256
//                .compact();
//    }
//
//
//    // Giải mã JWT
//    public static Claims decodeToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//
//    // kiem tra hop le token
//    public static boolean validateToken(String token) {
//        try {
//            decodeToken(token);
//            return true; // token hop le
//        } catch (Exception e) {
//            return false; // token khong hop le
//        }
//    }
//
////        public static void main(String[] args) {
////            // Tạo token
////            String token = JWTUtil.generateToken("user123");
////            System.out.println("Generated Token: " + token);
////
////            // Giải mã token
////            Claims claims = JWTUtil.decodeToken(token);
////            System.out.println("User: " + claims.getSubject());
////
////            // Kiểm tra token
////            System.out.println("Is valid: " + JWTUtil.validateToken(token));
////        }
//
//}
//
