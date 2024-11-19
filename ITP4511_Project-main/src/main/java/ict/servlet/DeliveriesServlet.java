/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet;

import ict.bean.DeliveryBean;
import ict.db.DeliveryDB;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "DeliveriesServlet", urlPatterns = {"/DeliveriesServlet"})
public class DeliveriesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        // Assuming the user ID is stored in the session
        int userId = (int) request.getSession().getAttribute("userId");
        
        try {
            DeliveryDB deliveryDB = new DeliveryDB("jdbc:mysql://localhost:3306/ITP4511_Project", "root", ""); // Adjust parameters as needed
//            List<DeliveryBean> deliveries = deliveryDB.getAllDeliveries();
            List<DeliveryBean> deliveries = deliveryDB.getDeliveriesByUserId(userId);
            JSONArray jsonArray = new JSONArray();
            for (DeliveryBean delivery : deliveries) {
                JSONObject json = new JSONObject();
                json.put("deliveryId", delivery.getDeliveryId());
                json.put("bookingId", delivery.getBookingId());
                json.put("courierId", delivery.getCourierId());
                json.put("courierName", delivery.getCourierName());
                json.put("pickupLocation", delivery.getPickupLocation());
                json.put("status", delivery.getStatus());
                json.put("scheduledTime", delivery.getScheduledTime() != null ? delivery.getScheduledTime().toString() : JSONObject.NULL);
                json.put("deliveredTime", delivery.getDeliveredTime() != null ? delivery.getDeliveredTime().toString() : JSONObject.NULL);
                json.put("createdAt", delivery.getCreatedAt().toString());
                json.put("updatedAt", delivery.getUpdatedAt().toString());
                jsonArray.put(json);
            }

            try (java.io.PrintWriter out = response.getWriter()) {
                out.print(jsonArray.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error: " + e.getMessage());
        }
    }
}
