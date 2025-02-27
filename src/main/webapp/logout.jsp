<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Logout</title>
</head>
<body>
<script>
    // Xóa cookie bằng cách đặt maxAge = 0 (phía client không thể truy cập cookie HttpOnly, nên vẫn cần servlet)
    document.cookie = "accessToken=; max-age=0; path=/";
    document.cookie = "refreshToken=; max-age=0; path=/";
    window.location.href = "login.jsp";
</script>
</body>
</html>