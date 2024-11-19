package ict.db;

import ict.bean.EquipmentBean;
import java.sql.*;
import java.util.ArrayList;

public class EquipmentDB {

    private String dburl;
    private String dbUser;
    private String dbPassword;

    public EquipmentDB() {
        // Default constructor with default database connection parameters
        this.dburl = "jdbc:mysql://localhost:3306/ITP4511_Project";
        this.dbUser = "root";
        this.dbPassword = ""; // Default password for XAMPP's MySQL
    }

    public EquipmentDB(String dburl, String dbUser, String dbPassword) {
        this.dburl = dburl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    private EquipmentBean extractEquipment(ResultSet rs) throws SQLException {
        EquipmentBean equipment = new EquipmentBean();
        equipment.setEquipmentId(rs.getInt("equipment_id"));
        equipment.setName(rs.getString("name"));
        equipment.setDescription(rs.getString("description"));
        equipment.setStatus(rs.getString("status"));
        equipment.setLocation(rs.getString("location"));
        equipment.setStaffOnly(rs.getBoolean("staff_only"));
        return equipment;
    }

    public ArrayList<EquipmentBean> getAllEquipment() {
        ArrayList<EquipmentBean> equipments = new ArrayList<>();
        String sql = "SELECT * FROM Equipment";
        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                equipments.add(extractEquipment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipments;
    }

    public ArrayList<EquipmentBean> getEquipmentNamesAndIds() {
        ArrayList<EquipmentBean> equipments = new ArrayList<>();
        String sql = "SELECT equipment_id, name FROM Equipment";
        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EquipmentBean equipment = new EquipmentBean();
                equipment.setEquipmentId(rs.getInt("equipment_id"));
                equipment.setName(rs.getString("name"));
                equipments.add(equipment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipments;
    }

    public EquipmentBean getEquipmentById(int id) {
        EquipmentBean equipment = null;
        String sql = "SELECT * FROM Equipment WHERE equipment_id = ?";
        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                equipment = extractEquipment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipment;
    }

    public void addOrUpdateEquipment(EquipmentBean equipment) throws SQLException {
        String sql;
        if (equipment.getEquipmentId() > 0) {
            // Update existing equipment
            sql = "UPDATE Equipment SET name = ?, description = ?, status = ?, location = ?, staff_only = ? WHERE equipment_id = ?";
        } else {
            // Insert new equipment
            sql = "INSERT INTO Equipment (name, description, status, location, staff_only) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, equipment.getName());
            ps.setString(2, equipment.getDescription());
            ps.setString(3, equipment.getStatus());
            ps.setString(4, equipment.getLocation());
            ps.setBoolean(5, equipment.isStaffOnly());

            if (equipment.getEquipmentId() > 0) {
                ps.setInt(6, equipment.getEquipmentId());
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating or updating equipment failed, no rows affected.");
            }

            if (equipment.getEquipmentId() == 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        equipment.setEquipmentId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating equipment failed, no ID obtained.");
                    }
                }
            }
        }
    }
}
