<%-- 
    Document   : main
    Created on : 2024年5月2日, 上午10:17:41
    Author     : puinamkwok
--%>

<%@ page import="ict.bean.BookingBean" %>
<%@ page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Main page</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/main.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <jsp:useBean id="userInfo" class="ict.bean.UserInfo" scope="session"/>
    <div class="header">
        <h1>Hello, <%= userInfo.getUsername() %></h1>
        <h2>Your role is: <%= userInfo.getRole() %></h2>
        <h2>Your User id: <%= userInfo.getUserId() %></h2>
    </div>
   
    <div class="container">
        <% if (!"courier".equals(userInfo.getRole())) { %>
            <h1>Your Bookings</h1>
                <div id="bookings-container">
                    <!-- Booking data will be inserted here by JavaScript -->
                </div>
        <% } %>
    </div>
    
    <script src="js/main.js"></script>
</body>
</html>
