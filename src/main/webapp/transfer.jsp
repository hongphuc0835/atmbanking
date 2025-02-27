<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transfer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
<div class="container">
    <div class="row justify-content-center mt-5">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-body">
                    <h2 class="card-title text-center mb-4">Transfer Money from Account: ${param.accountNumber}</h2>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger mt-3">${error}</div>
                    </c:if>
                    <form action="TransferServlet" method="post">
                        <input type="hidden" name="accountNumber" value="${param.accountNumber}">
                        <div class="mb-3">
                            <label for="targetAccount" class="form-label">Target Account</label>
                            <input type="text" class="form-control" id="targetAccount" name="targetAccount" required>
                        </div>
                        <div class="mb-3">
                            <label for="amount" class="form-label">Amount</label>
                            <input type="number" class="form-control" id="amount" name="amount" step="0.01" required>
                        </div>
                        <button type="submit" class="btn btn-success w-100">Transfer</button>
                        <a href="dashboard" class="btn btn-secondary w-100 mt-3">Back to Dashboard</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>