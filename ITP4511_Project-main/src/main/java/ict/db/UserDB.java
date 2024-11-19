/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.db;

import ict.bean.UserInfo;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author puinamkwok
 */
public class UserDB {

    private String dburl;
    private String dbUser;
    private String dbPassword;
    private Connection connection; // Add connection variable

    public UserDB(String dburl, String dbUser, String dbPassword) {
        this.dburl = dburl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public UserInfo getUserById(int userId) {
        UserInfo userInfo = null;
        String sql = "SELECT * FROM Users WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userInfo = new UserInfo();
                userInfo.setUserId(rs.getInt("user_id"));
                userInfo.setUsername(rs.getString("username"));
                userInfo.setPassword(rs.getString("password"));  // Consider security implications
                userInfo.setRole(rs.getString("role"));
                userInfo.setFirstName(rs.getString("first_name"));
                userInfo.setLastName(rs.getString("last_name"));
                userInfo.setEmail(rs.getString("email"));
                userInfo.setPhoneNumber(rs.getString("phone_number"));
                userInfo.setCreatedAt(rs.getTimestamp("created_at"));
                userInfo.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public UserInfo getUserByUsernameAndPassword(String username, String password) {
        // 實現JDBC連接、執行SQL查詢並返回UserBean
        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(rs.getInt("user_id"));
                userInfo.setUsername(rs.getString("username"));
                userInfo.setPassword(rs.getString("password"));
                userInfo.setFirstName(rs.getString("first_name"));
                userInfo.setLastName(rs.getString("last_name"));
                userInfo.setEmail(rs.getString("email"));
                userInfo.setPhoneNumber(rs.getString("phone_number"));
                userInfo.setRole(rs.getString("role"));
                userInfo.setCreatedAt(rs.getTimestamp("created_at"));
                userInfo.setUpdatedAt(rs.getTimestamp("updated_at"));
                return userInfo;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean addUserInfo(String id, String user, String pwd) {
        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Users (ID, USERNAME, PASSWORD) VALUES (?, ?, ?)")) {
            stmt.setString(1, id);
            stmt.setString(2, user);
            stmt.setString(3, pwd);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(UserInfo userInfo) {
        StringBuilder sql = new StringBuilder("UPDATE Users SET updated_At = CURRENT_TIMESTAMP");

        if (userInfo.getRole() != null) {
            sql.append(", role = ?");
        }
        if (userInfo.getPassword() != null) {
            sql.append(", password = ?");
        }
        if (userInfo.getFirstName() != null) {
            sql.append(", first_Name = ?");
        }
        if (userInfo.getLastName() != null) {
            sql.append(", last_Name = ?");
        }
        if (userInfo.getEmail() != null) {
            sql.append(", email = ?");
        }
        if (userInfo.getPhoneNumber() != null) {
            sql.append(", phone_number = ?");
        }
        if (userInfo.getUsername() != null) {
            sql.append(", username = ?");
        }
        sql.append(" WHERE user_id = ?");

        try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPassword); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (userInfo.getRole() != null) {
                pstmt.setString(index++, userInfo.getPassword());
            }
            if (userInfo.getPassword() != null) {
                pstmt.setString(index++, userInfo.getPassword());
            }

            if (userInfo.getFirstName() != null) {
                pstmt.setString(index++, userInfo.getFirstName());
            }
            if (userInfo.getLastName() != null) {
                pstmt.setString(index++, userInfo.getLastName());
            }
            if (userInfo.getEmail() != null) {
                pstmt.setString(index++, userInfo.getEmail());
            }
            if (userInfo.getPhoneNumber() != null) {
                pstmt.setString(index++, userInfo.getPhoneNumber());
            }
            if (userInfo.getUsername() != null) {
                pstmt.setString(index++, userInfo.getUsername());
            }
            pstmt.setInt(index, userInfo.getUserId());

            int updated = pstmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
