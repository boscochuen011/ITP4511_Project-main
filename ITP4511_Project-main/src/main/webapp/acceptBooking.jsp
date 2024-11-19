<%-- 
    Document   : acceptBooking
    Created on : 2024年5月16日, 下午4:43:48
    Author     : boscochuen
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="header.jsp"/>
    <meta charset="UTF-8">
    <title>Accept Booking</title>
    <link rel="stylesheet" type="text/css" href="css/acceptBooking.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="js/acceptBooking.js"></script>
</head>
<body>
    <h1>Waiting for accept booking</h1>
    <table id="bookingTable">
        <!-- Booking data will be inserted here -->
    </table>

    <!-- The Modal -->
    <div id="editModal">
        <div id="modalContent">
            <span id="closeModalBtn">&times;</span>
            <h2>Edit Booking and Delivery Status</h2>
            <input type="hidden" id="bookingId">
            <input type="hidden" id="deliveryId">
            <p>Booking Status:</p>
            <select id="bookingStatus">
                <option value="pending">pending</option>
                <option value="approved">approved</option>
                <option value="denied">denied</option>
                <option value="completed">completed</option>
            </select>
            <p>Courier ID: <span id="courierId"></span></p>
            <p>Pickup Location: <span id="pickupLocation"></span></p>
            <p>Delivery Status:</p>
            <select id="deliveryStatus">
                <option value="scheduled">Scheduled</option>
                <option value="in_transit">In Transit</option>
                <option value="delivered">Delivered</option>
            </select>
            <button id="updateStatusBtn" class="button">Update</button>
        </div>
    </div>
</body>
</html>
