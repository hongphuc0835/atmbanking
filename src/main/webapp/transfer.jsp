<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chuyển khoản</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Thêm Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .card-header {
            background: linear-gradient(90deg, #ffc107, #e0a800);
        }
        .btn-success:hover {
            background-color: #e0a800;
            border-color: #d39e00;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
            border-color: #545b62;
        }
        .form-control:focus {
            border-color: #ffc107;
            box-shadow: 0 0 0 0.2rem rgba(255, 193, 7, 0.25);
        }
        .input-group-text {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-lg">
        <div class="card-header text-white d-flex justify-content-between align-items-center">
            <h3 class="mb-0"><i class="bi bi-arrow-left-right me-2"></i>Chuyển khoản từ tài khoản: ${param.accountNumber}</h3>
            <a href="dashboard" class="btn btn-outline-light btn-sm"><i class="bi bi-arrow-left"></i> Quay lại</a>
        </div>
        <div class="card-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <form action="TransferServlet" method="post">
                <input type="hidden" name="accountNumber" value="${param.accountNumber}">
                <div class="mb-4">
                    <label for="targetAccount" class="form-label fw-bold">Tài khoản đích</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-person"></i></span>
                        <input type="text" class="form-control" id="targetAccount" name="targetAccount" placeholder="Nhập số tài khoản đích" required>
                    </div>
                </div>
                <div class="mb-4">
                    <label for="amount" class="form-label fw-bold">Số tiền</label>
                    <div class="input-group">
                        <span class="input-group-text">$</span>
                        <input type="number" step="0.01" class="form-control" id="amount" name="amount" placeholder="Nhập số tiền" required>
                    </div>
                    <div class="form-text">Vui lòng nhập số tiền lớn hơn 0 (tối đa 2 chữ số thập phân).</div>
                </div>
                <div class="d-flex gap-3">
                    <button type="submit" class="btn btn-success flex-grow-1">
                        <i class="bi bi-arrow-right-circle me-2"></i>Chuyển khoản
                    </button>
                    <a href="dashboard" class="btn btn-secondary flex-grow-1">
                        <i class="bi bi-x-circle me-2"></i>Hủy
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>