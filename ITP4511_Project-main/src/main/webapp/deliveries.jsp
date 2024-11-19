<%-- 
    Document   : deliveries
    Created on : 2024?5?18?, ??3:49:32
    Author     : boscochuen
--%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Delivery List</title>
        <link href="css/Delivery.css" rel="stylesheet">
        <script src="js/Delivery.js"></script>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <h1>Delivery List</h1>
        <table id="deliveriesTable">
            <thead>
                <tr>
                    <th class="sortable">Delivery ID</th>
                    <th class="sortable">Booking ID</th>
                    <th>Courier Name</th>
                    <th>Pickup Location</th>
                    <th>Status</th>
                    <th class="sortable">Scheduled Time</th>
                    <th class="sortable">Delivered Time</th>
                    <th>Created At</th>
                    <th>Updated At</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <!-- Modal for editing status -->
        <div id="statusModal" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Edit Status</h2>
                <form id="statusForm">
                    <input type="hidden" id="editDeliveryId" name="deliveryId">
                    <label for="editStatus">Status:</label>
                    <select id="editStatus" name="status">
                        <option value="scheduled">Scheduled</option>
                        <option value="in_transit">In Transit</option>
                        <option value="delivered">Delivered</option>
                    </select>
                    <button type="submit">Save</button>
                </form>
            </div>
        </div>
    </body>
</html>
