<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <h3 class="mb-0">Welcome, ${user.username}</h3>
            <a href="logout" class="btn btn-outline-light btn-sm">Logout</a>
        </div>
        <div class="card-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="card-title text-muted mb-0">Your Accounts</h4>
                <form action="createAccount" method="post" class="mb-0">
                    <button type="submit" class="btn btn-success">Create New Account</button>
                </form>
            </div>

            <div class="table-responsive">
                <table class="table table-hover table-bordered">
                    <thead class="table-dark">
                    <tr>
                        <th scope="col">Account Number</th>
                        <th scope="col">Balance</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="account" items="${accounts}">
                        <tr>
                            <td>${account.accountNumber}</td>
                            <td class="text-success fw-bold">$${account.balance}</td>
                            <td>
                                <div class="d-flex gap-2">
                                    <a href="withdraw.jsp?accountNumber=${account.accountNumber}" class="btn btn-primary btn-sm">Withdraw</a>
                                    <a href="deposit.jsp?accountNumber=${account.accountNumber}" class="btn btn-success btn-sm">Deposit</a>
                                    <a href="transfer.jsp?accountNumber=${account.accountNumber}" class="btn btn-warning btn-sm">Transfer</a>
                                    <a href="HistoryServlet?accountNumber=${account.accountNumber}" class="btn btn-info btn-sm">History</a>
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