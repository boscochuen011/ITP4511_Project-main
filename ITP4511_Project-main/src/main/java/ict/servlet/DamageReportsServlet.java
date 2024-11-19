/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/WebService.java to edit this template
 */
package ict.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import ict.db.DamageReportDB;
import ict.bean.DamageReportsBean;

@WebServlet("/damageReports")
public class DamageReportsServlet extends HttpServlet {

    private DamageReportDB damageReportDB;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dbUser = this.getServletContext().getInitParameter("dbUser");
            String dbPassword = this.getServletContext().getInitParameter("dbPassword");
            String dbUrl = this.getServletContext().getInitParameter("dbUrl");
            System.out.println("Database URL: " + dbUrl); // Debugging line
            damageReportDB = new DamageReportDB(dbUrl, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC driver not found", e);
        } catch (Exception e) {
            throw new ServletException("Error initializing servlet: " + e.getMessage(), e);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            List<DamageReportsBean> reports = damageReportDB.getAllDamageReports();
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");
            for (int i = 0; i < reports.size(); i++) {
                DamageReportsBean report = reports.get(i);
                jsonBuilder.append("{");
                jsonBuilder.append("\"reportId\":").append(report.getReportId()).append(",");
                jsonBuilder.append("\"equipmentId\":").append(report.getEquipmentId()).append(",");
                jsonBuilder.append("\"reportedBy\":").append(report.getReportedBy()).append(",");
                jsonBuilder.append("\"description\":").append("\"").append(escapeJson(report.getDescription())).append("\"").append(",");
                jsonBuilder.append("\"reportDate\":").append("\"").append(report.getReportDate().toString()).append("\"").append(",");
                jsonBuilder.append("\"status\":").append("\"").append(report.getStatus()).append("\"").append(",");
                jsonBuilder.append("\"createdAt\":").append("\"").append(report.getCreatedAt().toString()).append("\"").append(",");
                jsonBuilder.append("\"updatedAt\":").append("\"").append(report.getUpdatedAt().toString()).append("\"");
                jsonBuilder.append("}");
                if (i < reports.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]");
            out.print(jsonBuilder.toString());
            out.flush();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\":\"Internal server error occurred while accessing data. Details: " + e.getMessage() + "\"}");
                out.flush();
            }
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int reportId = Integer.parseInt(request.getParameter("reportId"));
    String status = request.getParameter("status");
    try {
        damageReportDB.updateDamageReportStatus(reportId, status);
        response.getWriter().write("Success");
    } catch (SQLException e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Error updating report: " + e.getMessage());
    }
}


    private String escapeJson(String data) {
        if (data == null) {
            return "";
        }
        return data.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
