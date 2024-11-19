/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.db;

import ict.bean.DeliveryBean;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDB {

    private String dburl;
    private String dbUser;
    private String dbPassword;

    public DeliveryDB(String dburl, String dbUser, String dbPassword) {
        this.dburl = dburl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public void createDelivery(int bookingId) throws SQLException {
        // 查询获取任意一个courier的user_id
        String fetchCourierSQL = "SELECT user_id FROM Users WHERE role = 'courier' ORDER BY user_id LIMIT 1";

        // 查询获取特定booking的delivery_location
        String fetchBookingSQL = "SELECT delivery_location FROM Bookings WHERE booking_id = ?";

        // 插入新的Delivery记录
        String insertDeliverySQL = "INSERT INTO Deliveries (booking_id, courier_id, pickup_location, status, scheduled_time) "
                + "VALUES (?, ?, ?, 'scheduled', CURRENT_TIMESTAMP)";

        try (Connection connection = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement fetchCourierStmt = connection.prepareStatement(fetchCourierSQL); PreparedStatement fetchBookingStmt = connection.prepareStatement(fetchBookingSQL); PreparedStatement insertDeliveryStmt = connection.prepareStatement(insertDeliverySQL)) {

            // 获取任意一个courier的user_id
            ResultSet courierResultSet = fetchCourierStmt.executeQuery();
            int courierId = 0;
            if (courierResultSet.next()) {
                courierId = courierResultSet.getInt("user_id");
            } else {
                throw new SQLException("No courier available.");
            }

            // 获取特定booking的delivery_location
            fetchBookingStmt.setInt(1, bookingId);
            ResultSet bookingResultSet = fetchBookingStmt.executeQuery();
            if (bookingResultSet.next()) {
                String pickupLocation = bookingResultSet.getString("delivery_location");

                // 插入新的Delivery记录
                insertDeliveryStmt.setInt(1, bookingId);
                insertDeliveryStmt.setInt(2, courierId);
                insertDeliveryStmt.setString(3, pickupLocation);
                insertDeliveryStmt.executeUpdate();
            } else {
                System.out.println("Booking ID not found: " + bookingId);
                throw new SQLException("Booking ID not found: " + bookingId);
            }

        }
    }

    public DeliveryBean getDeliveryByBookingId(int bookingId) throws SQLException {
        DeliveryBean delivery = null;
        String sql = "SELECT * FROM Deliveries WHERE booking_id = ?";
        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                delivery = new DeliveryBean();
                delivery.setDeliveryId(rs.getInt("delivery_id"));
                delivery.setCourierId(rs.getInt("courier_id"));
                delivery.setPickupLocation(rs.getString("pickup_location"));
                delivery.setStatus(rs.getString("status"));
                // Consider adding other fields here as well.
            } else {
                throw new SQLException("No delivery found for booking ID: " + bookingId);
            }
        }
        return delivery;
    }

    public void updateDeliveryStatus(int deliveryId, String status) throws SQLException {
        String sql = "UPDATE Deliveries SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE delivery_id = ?";
        try (Connection connection = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, deliveryId);
            statement.executeUpdate();
        }
    }

    public List<DeliveryBean> getAllDeliveries() throws SQLException {
        List<DeliveryBean> deliveries = new ArrayList<>();
        String sql = "SELECT d.*, u.first_name, u.last_name FROM Deliveries d "
                + "JOIN Users u ON d.courier_id = u.user_id";

        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DeliveryBean delivery = new DeliveryBean();
                delivery.setDeliveryId(rs.getInt("delivery_id"));
                delivery.setBookingId(rs.getInt("booking_id"));
                delivery.setCourierId(rs.getInt("courier_id"));
                delivery.setPickupLocation(rs.getString("pickup_location"));
                delivery.setStatus(rs.getString("status"));
                delivery.setScheduledTime(rs.getTimestamp("scheduled_time"));
                delivery.setDeliveredTime(rs.getTimestamp("delivered_time"));
                delivery.setCreatedAt(rs.getTimestamp("created_at"));
                delivery.setUpdatedAt(rs.getTimestamp("updated_at"));
                String courierName = rs.getString("first_name") + " " + rs.getString("last_name");
                delivery.setCourierName(courierName);
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }

    public List<DeliveryBean> getDeliveriesByUserId(int userId) throws SQLException {
        List<DeliveryBean> deliveries = new ArrayList<>();
        String sql = "SELECT d.*, u.first_name, u.last_name FROM Deliveries d "
                + "JOIN Users u ON d.courier_id = u.user_id "
                + "WHERE d.courier_id = ?";

        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DeliveryBean delivery = new DeliveryBean();
                delivery.setDeliveryId(rs.getInt("delivery_id"));
                delivery.setBookingId(rs.getInt("booking_id"));
                delivery.setCourierId(rs.getInt("courier_id"));
                delivery.setPickupLocation(rs.getString("pickup_location"));
                delivery.setStatus(rs.getString("status"));
                delivery.setScheduledTime(rs.getTimestamp("scheduled_time"));
                delivery.setDeliveredTime(rs.getTimestamp("delivered_time"));
                delivery.setCreatedAt(rs.getTimestamp("created_at"));
                delivery.setUpdatedAt(rs.getTimestamp("updated_at"));
                String courierName = rs.getString("first_name") + " " + rs.getString("last_name");
                delivery.setCourierName(courierName);
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }

    public void updateDeliveryStatus(int deliveryId, String status, String deliveredTime) throws SQLException {
        // Start by setting the SQL to only update the status and the generic update timestamp
        String sql = "UPDATE Deliveries SET status = ?, updated_at = CURRENT_TIMESTAMP";

        // If the status is 'delivered' and a deliveredTime is provided, add that field to the update
        if (deliveredTime != null && status.equals("delivered")) {
            sql += ", delivered_time = ?";
        }

        sql += " WHERE delivery_id = ?";

        try (Connection connection = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status); // Set the status

            // Check if deliveredTime needs to be set
            if (deliveredTime != null && status.equals("delivered")) {
                statement.setTimestamp(2, Timestamp.from(Instant.parse(deliveredTime))); // Set the delivered time
                statement.setInt(3, deliveryId); // Set the WHERE clause parameter for delivery_id
            } else {
                statement.setInt(2, deliveryId); // Set the WHERE clause parameter for delivery_id if no deliveredTime
            }

            statement.executeUpdate(); // Execute the update
        }
    }

}
