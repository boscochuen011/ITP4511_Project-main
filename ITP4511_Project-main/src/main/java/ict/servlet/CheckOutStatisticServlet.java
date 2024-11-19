package ict.servlet;

import com.google.gson.Gson;
import ict.bean.CheckoutStatistic;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CheckOutStatistic")
public class CheckOutStatisticServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ITP4511_Project";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String year = request.getParameter("year");
        String month = request.getParameter("month");
        List<CheckoutStatistic> statistics = new ArrayList<>();

        String sql = "SELECT e.name AS equipment_name, COUNT(b.booking_id) AS checkouts " +
                     "FROM Bookings b JOIN Equipment e ON b.equipment_id = e.equipment_id " +
                     "WHERE YEAR(b.created_at) = ? AND MONTH(b.created_at) = ? " +
                     "GROUP BY e.name";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, year);
            stmt.setString(2, month);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                statistics.add(new CheckoutStatistic(rs.getString("equipment_name"), rs.getInt("checkouts")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\":\"Database error occurred: " + e.getMessage() + "\"}");
            return;
        }

        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(statistics));
        out.flush();
    }
}
