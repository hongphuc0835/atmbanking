<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lịch sử giao dịch</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Thêm Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .card-header {
            background: linear-gradient(90deg, #17a2b8, #138496);
        }
        .btn-secondary:hover {
            background-color: #5a6268;
            border-color: #545b62;
        }
        .table-hover tbody tr:hover {
            background-color: #f1f8ff;
        }
        .text-amount {
            font-weight: bold;
        }
    </style>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-lg">
        <div class="card-header text-white d-flex justify-content-between align-items-center">
            <h3 class="mb-0"><i class="bi bi-clock-history me-2"></i>Lịch sử giao dịch tài khoản: ${accountNumber}</h3>
            <a href="dashboard" class="btn btn-outline-light btn-sm"><i class="bi bi-arrow-left"></i> Quay lại</a>
        </div>
        <div class="card-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <div class="table-responsive">
                <table class="table table-striped table-bordered table-hover">
                    <thead class="table-dark">
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Loại giao dịch</th>
                        <th scope="col">Số tiền</th>
                        <th scope="col">Tài khoản đích</th>
                        <th scope="col">Ngày giờ</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="tx" items="${transactions}">
                        <tr>
                            <td>${tx.id}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${tx.type == 'DEPOSIT'}">
                                        <span class="badge bg-success"><i class="bi bi-piggy-bank me-1"></i>Nạp tiền</span>
                                    </c:when>
                                    <c:when test="${tx.type == 'WITHDRAW'}">
                                        <span class="badge bg-primary"><i class="bi bi-cash-stack me-1"></i>Rút tiền</span>
                                    </c:when>
                                    <c:when test="${tx.type == 'TRANSFER'}">
                                        <span class="badge bg-warning"><i class="bi bi-arrow-left-right me-1"></i>Chuyển khoản</span>
                                    </c:when>
                                    <c:otherwise>${tx.type}</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-amount text-success">$${tx.amount}</td>
                            <td>${tx.targetAccount != null ? tx.targetAccount : '-'}</td>
                            <td><fmt:formatDate value="${tx.transactionDate}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <a href="dashboard" class="btn btn-secondary w-100 mt-3">
                <i class="bi bi-house-door me-2"></i>Quay lại Dashboard
            </a>
        </div>
    </div>
</div>
</body>
</html>