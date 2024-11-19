/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet;

/**
 *
 * @author boscochuen
 */


import ict.db.DeliveryDB;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet(name = "UpdateDeliveryStatusServlet", urlPatterns = {"/UpdateDeliveryStatusServlet"})
public class UpdateDeliveryStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            JSONObject jsonRequest = new JSONObject(request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual));
            int deliveryId = jsonRequest.getInt("deliveryId");
            String status = jsonRequest.getString("status");
            String deliveredTime = jsonRequest.optString("deliveredTime", null);

            DeliveryDB deliveryDB = new DeliveryDB("jdbc:mysql://localhost:3306/ITP4511_Project", "root", "");
            deliveryDB.updateDeliveryStatus(deliveryId, status, deliveredTime);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", true);

            try (java.io.PrintWriter out = response.getWriter()) {
                out.print(jsonResponse.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error: " + e.getMessage());
        }
    }
}
