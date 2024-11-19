package ict.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ReportDamageServlet")
public class ReportDamageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dbUrl = "jdbc:mysql://localhost:3306/ITP4511_Project";
        String user = "root";
        String password = ""; // XAMPP's MySQL default password is usually empty

        String equipmentId = request.getParameter("equipmentId");
        String reportedBy = request.getParameter("reportedBy");
        String description = request.getParameter("description");

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
            String sql = "INSERT INTO DamageReports (equipment_id, reported_by, description) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(equipmentId));
                stmt.setInt(2, Integer.parseInt(reportedBy));
                stmt.setString(3, description);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("inventory.jsp");
    }
}
