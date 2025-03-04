<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Thêm Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <h3 class="mb-0">Chào mừng, ${user.username}</h3>
            <a href="logout" class="btn btn-outline-light btn-sm">
                <i class="bi bi-box-arrow-right"></i> Đăng xuất
            </a>
        </div>
        <div class="card-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="card-title text-muted mb-0">Tài khoản của bạn</h4>
                <form action="createAccount" method="post" class="mb-0">
                    <button type="submit" class="btn btn-success">
                        <i class="bi bi-plus-circle"></i> Tạo tài khoản mới
                    </button>
                </form>
            </div>

            <div class="table-responsive">
                <table class="table table-hover table-bordered">
                    <thead class="table-dark">
                    <tr>
                        <th scope="col">Số tài khoản</th>
                        <th scope="col">Số dư</th>
                        <th scope="col">Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="account" items="${accounts}">
                        <tr>
                            <td>${account.accountNumber}</td>
                            <td class="text-success fw-bold">$${account.balance}</td>
                            <td>
                                <div class="d-flex gap-2">
                                    <a href="withdraw.jsp?accountNumber=${account.accountNumber}" class="btn btn-primary btn-sm">
                                        <i class="bi bi-cash-stack"></i> Rút tiền
                                    </a>
                                    <a href="deposit.jsp?accountNumber=${account.accountNumber}" class="btn btn-success btn-sm">
                                        <i class="bi bi-piggy-bank"></i> Nạp tiền
                                    </a>
                                    <a href="transfer.jsp?accountNumber=${account.accountNumber}" class="btn btn-warning btn-sm">
                                        <i class="bi bi-arrow-left-right"></i> Chuyển khoản
                                    </a>
                                    <a href="HistoryServlet?accountNumber=${account.accountNumber}" class="btn btn-info btn-sm">
                                        <i class="bi bi-clock-history"></i> Lịch sử
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>