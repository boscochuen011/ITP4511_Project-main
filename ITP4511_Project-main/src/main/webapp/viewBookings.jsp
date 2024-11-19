<%-- 
    Document   : viewBookings
    Created on : 2024?5?3?, ??9:15:00
    Author     : user
--%>

<%@ page import="java.util.List" %>
<%@ page import="ict.bean.BookingBean" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Booking Records</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
    <jsp:include page="header.jsp"/>
    <br>
    <div class="container">
        <h1>My Booking Records</h1>

        <%
            List<BookingBean> bookings = (List<BookingBean>) request.getAttribute("bookings");
            if (bookings != null && !bookings.isEmpty()) {
        %>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Equipment Name</th>
                    <th>Start Time</th>
                    <th>End Time</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <% for (BookingBean booking : bookings) { %>
                <tr>
                    <td><%= booking.getEquipmentName() %></td>
                    <td><%= booking.getStartTime() %></td>
                    <td><%= booking.getEndTime() %></td>
                    <td><%= booking.getStatus() %></td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <% } else { %>

        <p>No booking records found.</p>

        <% } %>

    </div>

    </body>
</html>
