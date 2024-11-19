package ict.servlet;

import com.google.gson.Gson;
import ict.bean.BookingBean;
import ict.bean.UserInfo;
import ict.db.BookingDB;
import ict.db.DeliveryDB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

/**
 *
 * @author user
 */
@WebServlet(name = "BookingServlet", urlPatterns = {"/booking"})
public class BookingServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "view":
                showBookings(request, response);
                break;
            case "listBookings":
                listBookings(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
                break;
        }
    }

    private void showBookings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<BookingBean> bookings = bookingDB.getBookingsByUserId(userInfo.getUserId());
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("/viewBookings.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to retrieve bookings", e);
        }
    }
    
    private void listBookings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
            if (userInfo == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            List<BookingBean> bookings = bookingDB.getActiveBookingsByUser(userInfo.getUserId());
            
            // Convert bookings list to JSON
            Gson gson = new Gson();
            String jsonBookings = gson.toJson(bookings);

            // Prepare the JSON response
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("success", true);
            jsonResponse.put("bookings", bookings);

            String jsonResponseString = gson.toJson(jsonResponse);

            // Send JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.write(jsonResponseString);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "create":
                createBooking(request, response);
                break;
            case "update":
                updateBooking(request, response);
                break;
            case "cancel":
                cancelBooking(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

    private void createBooking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("user_id"));
        int equipmentId = Integer.parseInt(request.getParameter("equipment_id"));
        String startTimeStr = request.getParameter("start_time");
        String endTimeStr = request.getParameter("end_time");
        String deliveryLocation = request.getParameter("delivery_location");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date parsedStartDate = dateFormat.parse(startTimeStr);
            Timestamp startTime = new Timestamp(parsedStartDate.getTime());
            Date parsedEndDate = dateFormat.parse(endTimeStr);
            Timestamp endTime = new Timestamp(parsedEndDate.getTime());

            BookingBean booking = new BookingBean();
            booking.setUserId(userId);
            booking.setEquipmentId(equipmentId);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setDeliveryLocation(deliveryLocation);
            booking.setStatus("pending");  // Set status as pending initially or based on some business logic

            int bookingId = bookingDB.saveBooking(booking);

            // Create a delivery entry for the new booking if the status is pending
            if ("pending".equals(booking.getStatus())) {
                deliveryDB.createDelivery(bookingId);
            }

            response.sendRedirect("equipment?action=listAvailable"); // Adjust the redirection URL as necessary
        } catch (ParseException e) {
            throw new ServletException("Unable to parse date format", e);
        } catch (SQLException e) {
            throw new ServletException("Unable to save booking or create delivery", e);
        }

    }

    private void updateBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            try {
                int bookingId = Integer.parseInt(request.getParameter("bookingId"));
                bookingDB.updateBookingStatus(bookingId, "completed");
                out.print("{\"success\": true}");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"error\": \"Invalid booking ID\"}");
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"error\": \"Database error occurred\"}");
            }
        }
    }

    private void cancelBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            try {
                int bookingId = Integer.parseInt(request.getParameter("bookingId"));
                bookingDB.updateBookingStatus(bookingId, "denied");
                out.print("{\"success\": true}");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"error\": \"Invalid booking ID\"}");
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"error\": \"Database error occurred\"}");
            }
        }
    }
}
