/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.db;

/**
 *
 * @author boscochuen
 */
import ict.bean.DamageReportsBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DamageReportDB {

    private String dburl;
    private String dbUser;
    private String dbPassword;

    // Constructor to initialize the database credentials
    public DamageReportDB(String dburl, String dbUser, String dbPassword) {
        this.dburl = dburl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    // Method to create a database connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dburl, dbUser, dbPassword);
    }

    public List<DamageReportsBean> getAllDamageReports() throws SQLException {
        List<DamageReportsBean> damageReports = new ArrayList<>();
        String sql = "SELECT * FROM DamageReports";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println("Reading damage report: " + rs.getInt("report_id")); // Debugging line
                DamageReportsBean report = new DamageReportsBean(
                        rs.getInt("report_id"),
                        rs.getInt("equipment_id"),
                        rs.getInt("reported_by"),
                        rs.getString("description"),
                        rs.getTimestamp("report_date"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                damageReports.add(report);
            }
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage()); // Debugging line
            throw e; // rethrow the exception to handle it in servlet
        }
        return damageReports;
    }

    // Method to add a new damage report
    public boolean addDamageReport(DamageReportsBean report) throws SQLException {
        String sql = "INSERT INTO DamageReports (equipment_id, reported_by, description, status) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, report.getEquipmentId());
            ps.setInt(2, report.getReportedBy());
            ps.setString(3, report.getDescription());
            ps.setString(4, report.getStatus());
            int result = ps.executeUpdate();
            return result > 0;
        }
    }

    // Method to update an existing damage report
    public boolean updateDamageReport(DamageReportsBean report) throws SQLException {
        String sql = "UPDATE DamageReports SET description = ?, status = ? WHERE report_id = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, report.getDescription());
            ps.setString(2, report.getStatus());
            ps.setInt(3, report.getReportId());
            int result = ps.executeUpdate();
            return result > 0;
        }
    }

    public boolean updateDamageReportStatus(int reportId, String status) throws SQLException {
        String sql = "UPDATE DamageReports SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE report_id = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reportId);
            return ps.executeUpdate() > 0;
        }
    }

}
