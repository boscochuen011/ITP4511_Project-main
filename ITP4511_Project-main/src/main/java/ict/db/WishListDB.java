/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.db;

import ict.bean.EquipmentBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author puinamkwok
 */
public class WishListDB {
    private String dburl;
    private String dbUser;
    private String dbPassword;

    public WishListDB(String dburl, String dbUser, String dbPassword) {
        this.dburl = dburl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }
    
    public HashSet<Integer> getUserWishList(int userId) {
        HashSet<Integer> equipmentIds = new HashSet<>();
        String sql = "SELECT equipment_id FROM wishlist WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                equipmentIds.add(rs.getInt("equipment_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return equipmentIds;
    }
    
    public boolean toggleWishList(int userId, int equipmentId) {
        // Check if the item is already in the wishlist
        String checkSql = "SELECT count(1) FROM wishlist WHERE user_id = ? AND equipment_id = ?";
        try (Connection conn = getConnection();
        PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, equipmentId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // If exists, remove it
                String deleteSql = "DELETE FROM wishlist WHERE user_id = ? AND equipment_id = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, userId);
                    deleteStmt.setInt(2, equipmentId);
                    deleteStmt.executeUpdate();
                    return false; // Return false to indicate removal
                }
            } else {
                // If not exists, add it
                String insertSql = "INSERT INTO wishlist (user_id, equipment_id) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, equipmentId);
                    insertStmt.executeUpdate();
                    return true; // Return true to indicate addition
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error case
        }
    }  
    
    public List<EquipmentBean> getWishlistByUserId(int userId) {
        List<EquipmentBean> wishlist = new ArrayList<>();
        String sql = "SELECT e.* FROM Equipment e JOIN Wishlist w ON e.equipment_id = w.equipment_id WHERE w.user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                EquipmentBean equipment = new EquipmentBean();
                equipment.setEquipmentId(rs.getInt("equipment_id"));
                equipment.setName(rs.getString("name"));
                equipment.setDescription(rs.getString("description"));
                equipment.setStatus(rs.getString("status"));
                equipment.setLocation(rs.getString("location"));
                wishlist.add(equipment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wishlist;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dburl, dbUser, dbPassword);
    }
}
