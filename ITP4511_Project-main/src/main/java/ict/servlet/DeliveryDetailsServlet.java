/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet;

/**
 *
 * @author boscochuen
 */


import ict.bean.DeliveryBean;
import ict.db.DeliveryDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DeliveryDetailsServlet")
public class DeliveryDetailsServlet extends HttpServlet {
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
        deliveryDB = new DeliveryDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int bookingId = Integer.parseInt(request.getParameter("bookingId"));

        try (PrintWriter out = response.getWriter()) {
            DeliveryBean delivery = deliveryDB.getDeliveryByBookingId(bookingId);

            if (delivery != null) {
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder.add("deliveryId", delivery.getDeliveryId())
                                 .add("courierId", delivery.getCourierId())
                                 .add("pickupLocation", delivery.getPickupLocation())
                                 .add("status", delivery.getStatus());
                String json = jsonObjectBuilder.build().toString();
                out.print(json);
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Error retrieving delivery details", e);
        }
    }
}
