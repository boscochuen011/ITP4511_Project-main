package ict.db;

import ict.bean.BookingBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingDB handles database operations related to bookings.
 */
public class BookingDB {

    private String dburl;
    private String dbUser;
    private String dbPassword;

    public BookingDB(String dburl, String dbUser, String dbPassword) {
        this.dburl = dburl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public List<BookingBean> getBookingsByUserId(int userId) throws SQLException {
        List<BookingBean> bookings = new ArrayList<>();
        String sql = "SELECT b.*, e.name AS equipment_name FROM Bookings b JOIN Equipment e ON b.equipment_id = e.equipment_id WHERE b.user_id = ?";
        try (Connection connection = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BookingBean booking = new BookingBean();
                booking.setBookingId(resultSet.getInt("booking_id"));
                booking.setUserId(resultSet.getInt("user_id"));
                booking.setEquipmentId(resultSet.getInt("equipment_id"));
                booking.setEquipmentName(resultSet.getString("equipment_name"));
                booking.setStartTime(resultSet.getTimestamp("start_time"));
                booking.setEndTime(resultSet.getTimestamp("end_time"));
                booking.setDeliveryLocation(resultSet.getString("delivery_location"));
                booking.setStatus(resultSet.getString("status"));
                bookings.add(booking);
            }
        }
        return bookings;
    }
    
    public List<BookingBean> getActiveBookingsByUser(int userId) {
        List<BookingBean> bookings = new ArrayList<>();
        String sql = "SELECT b.*, e.name AS equipment_name FROM Bookings b JOIN Equipment e ON b.equipment_id = e.equipment_id WHERE b.user_id = ? AND b.status IN ('approved', 'pending')";
         try (Connection connection = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                BookingBean booking = new BookingBean();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setEquipmentId(rs.getInt("equipment_id"));
                booking.setEquipmentName(rs.getString("equipment_name"));
                booking.setStartTime(rs.getTimestamp("start_time"));
                booking.setEndTime(rs.getTimestamp("end_time"));
                booking.setDeliveryLocation(rs.getString("delivery_location"));
                booking.setStatus(rs.getString("status"));
                booking.setCreatedAt(rs.getTimestamp("created_at"));
                booking.setUpdatedAt(rs.getTimestamp("updated_at"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }


    public List<BookingBean> getAllBookings() throws SQLException {
        List<BookingBean> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.user_id, e.name AS equipment_name, b.start_time, b.end_time, b.delivery_location, b.status "
                + "FROM Bookings b "
                + "JOIN Equipment e ON b.equipment_id = e.equipment_id";
        try (Connection connection = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BookingBean booking = new BookingBean();
                booking.setBookingId(resultSet.getInt("booking_id"));
                booking.setUserId(resultSet.getInt("user_id"));
                booking.setEquipmentName(resultSet.getString("equipment_name"));
                booking.setStartTime(resultSet.getTimestamp("start_time"));
                booking.setEndTime(resultSet.getTimestamp("end_time"));
                booking.setDeliveryLocation(resultSet.getString("delivery_location"));
                booking.setStatus(resultSet.getString("status"));
                bookings.add(booking);
            }
        }
        return bookings;
    }

    public int saveBooking(BookingBean booking) throws SQLException {
        String sql = "INSERT INTO Bookings (user_id, equipment_id, start_time, end_time, delivery_location) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getEquipmentId());
            stmt.setTimestamp(3, booking.getStartTime());
            stmt.setTimestamp(4, booking.getEndTime());
            stmt.setString(5, booking.getDeliveryLocation());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
        }
    }

    public void updateBookingStatus(int bookingId, String status) throws SQLException {
        String sql = "UPDATE Bookings SET status = ? WHERE booking_id = ?";
        try (Connection connection = DriverManager.getConnection(dburl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, bookingId);
            statement.executeUpdate();
        }
    }
}
