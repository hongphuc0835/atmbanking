<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transaction History</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow">
        <div class="card-body">
            <h2 class="card-title text-center mb-4">Transaction History for Account: ${accountNumber}</h2>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <table class="table table-striped table-bordered">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Target Account</th>
                    <th>Date</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="tx" items="${transactions}">
                    <tr>
                        <td>${tx.id}</td>
                        <td>${tx.type}</td>
                        <td>${tx.amount}</td>
                        <td>${tx.targetAccount != null ? tx.targetAccount : '-'}</td>
                        <td><fmt:formatDate value="${tx.transactionDate}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <a href="dashboard" class="btn btn-secondary w-100 mt-3">Back to Dashboard</a>
        </div>
    </div>
</div>
</body>
</html>