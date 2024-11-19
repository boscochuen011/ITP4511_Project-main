package ict.servlet;

import ict.bean.BookingBean;
import ict.db.BookingDB;
import ict.db.DeliveryDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AcceptBookingServlet")
public class AcceptBookingServlet extends HttpServlet {
    private BookingDB bookingDB;
    private DeliveryDB deliveryDB;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("MySQL JDBC driver not found", e);
        }

        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        bookingDB = new BookingDB(dbUrl, dbUser, dbPassword);
        deliveryDB = new DeliveryDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            List<BookingBean> bookings = bookingDB.getAllBookings();  // Fetch all bookings

            // Use JSON-P to create JSON response
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (BookingBean booking : bookings) {
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder.add("bookingId", booking.getBookingId())
                                  .add("userId", booking.getUserId())
                                  .add("equipmentId", booking.getEquipmentId())
                                  .add("equipmentName", booking.getEquipmentName())
                                  .add("startTime", booking.getStartTime().toString())
                                  .add("endTime", booking.getEndTime() != null ? booking.getEndTime().toString() : "")
                                  .add("deliveryLocation", booking.getDeliveryLocation())
                                  .add("status", booking.getStatus());
                jsonArrayBuilder.add(jsonObjectBuilder);
            }
            String json = jsonArrayBuilder.build().toString();
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            throw new ServletException("Error retrieving bookings", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        int deliveryId = Integer.parseInt(request.getParameter("deliveryId"));
        String bookingStatus = request.getParameter("bookingStatus");
        String deliveryStatus = request.getParameter("deliveryStatus");

        try {
            bookingDB.updateBookingStatus(bookingId, bookingStatus);
            deliveryDB.updateDeliveryStatus(deliveryId, deliveryStatus);
            // Create a delivery record when booking is approved
            if ("approved".equalsIgnoreCase(bookingStatus)) {
                deliveryDB.createDelivery(bookingId);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            throw new ServletException("Error updating statuses", e);
        }
    }
}
