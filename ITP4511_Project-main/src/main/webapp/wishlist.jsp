<%-- 
    Document   : wishlist
    Created on : 2024年5月17日, 下午6:39:18
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="ict.bean.EquipmentBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.HashSet" %>
<!DOCTYPE html>
<html>
<head>
    <title>Wishlist</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <br/>
    <div class="container">
        <h1>Your Wishlist</h1>
        <table class="table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Location</th>
                </tr>
            </thead>
            <tbody>
                <% 
                List<EquipmentBean> wishlist = (List<EquipmentBean>) request.getAttribute("wishlistItems");
                if (wishlist != null) {
                    for (EquipmentBean equipment : wishlist) {
                %>
                <tr>
                    <td><%= equipment.getName() %></td>
                    <td><%= equipment.getDescription() %></td>
                    <td><%= equipment.getStatus() %></td>
                    <td><%= equipment.getLocation() %></td>
                </tr>
                <% 
                    }
                } else {
                %>
                <tr>
                    <td colspan="5" class="text-center">No items in your wishlist.</td>
                </tr>
                <% 
                }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>