<%-- 
    Document   : header
    Created on : 2024年5月2日, 下午6:14:34
    Author     : boscochuen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/header.css" rel="stylesheet"> 
</head>
<body>
    <jsp:useBean id="userInfo" class="ict.bean.UserInfo" scope="session"/>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="main.jsp">
                <img src="img/download.png" alt="Logo"> 
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="main.jsp">Home</a>
                    </li>
                    <% if (!"courier".equals(userInfo.getRole())) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="equipment?action=listAvailable">View Equipments</a>
                        </li>
                    <% } %>
                    <% if ("technician".equals(userInfo.getRole()) || "admin".equals(userInfo.getRole())) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="inventory.jsp">Inventory Records</a>
                        </li>
                    <% } %>
                    <% if ("technician".equals(userInfo.getRole()) || "admin".equals(userInfo.getRole())) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="acceptBooking.jsp">Accept Booking</a>
                        </li>
                    <% } %>
                    <% if ("admin".equals(userInfo.getRole())) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="checkOutStatistic.jsp">Check-out Statistic</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="createAccount.jsp">Manage Account</a>
                        </li>
                                             
                    <% } %>
                    <% if ("courier".equals(userInfo.getRole())) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="deliveries.jsp">My Task</a>
                        </li>
                    <% } %>
                    <li class="nav-item">
                        <a class="nav-link" href="profile.jsp">Profile</a>
                    </li>
                </ul>
                <form class="d-flex" method="post" action="main">
                    <input type="hidden" name="action" value="logout"/>
                    <button type="submit" class="btn btn-danger" name="logoutButton">Logout</button>
                </form>
            </div>
        </div>
    </nav>
    <!-- 引入 Popper 和 Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"></script>
</body>
</html>
