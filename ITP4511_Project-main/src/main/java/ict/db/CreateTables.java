/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.db;

import java.sql.*;

/**
 *
 * @author puinamkwok
 */
public class CreateTables {

    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://localhost:3306/ITP4511_Project";
        String user = "root";
        String password = ""; // XAMPP的MySQL默認密碼通常為空

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
            try (Statement stmt = conn.createStatement()) {
                // 創建Users表
                String createUsersTable = "CREATE TABLE IF NOT EXISTS Users ("
                        + "user_id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "username VARCHAR(255) UNIQUE NOT NULL,"
                        + "password VARCHAR(255) NOT NULL,"
                        + "role ENUM('user', 'staff', 'technician', 'admin', 'courier') NOT NULL,"
                        + "first_name VARCHAR(255),"
                        + "last_name VARCHAR(255),"
                        + "email VARCHAR(255) UNIQUE,"
                        + "phone_number VARCHAR(255),"
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                        + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                        + ");";
                stmt.execute(createUsersTable);

                // 創建Equipment表
                String createEquipmentTable = "CREATE TABLE IF NOT EXISTS Equipment ("
                        + "equipment_id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "name VARCHAR(255) NOT NULL,"
                        + "description TEXT,"
                        + "status ENUM('available', 'unavailable', 'maintenance', 'reserved') NOT NULL DEFAULT 'available',"
                        + "location VARCHAR(255) NOT NULL,"
                        + "staff_only BOOLEAN NOT NULL DEFAULT FALSE"
                        + ");";
                stmt.execute(createEquipmentTable);

                // 創建Bookings表
                String createBookingsTable = "CREATE TABLE IF NOT EXISTS Bookings ("
                        + "booking_id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "user_id INT,"
                        + "equipment_id INT,"
                        + "start_time TIMESTAMP,"
                        + "end_time TIMESTAMP NULL,"
                        + "delivery_location VARCHAR(255),"
                        + "status ENUM('pending', 'approved', 'denied', 'completed') DEFAULT 'pending',"
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                        + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                        + "FOREIGN KEY (user_id) REFERENCES Users(user_id),"
                        + "FOREIGN KEY (equipment_id) REFERENCES Equipment(equipment_id)"
                        + ");";
                stmt.execute(createBookingsTable);

                // 創建DamageReports表
                String createDamageReportsTable = "CREATE TABLE IF NOT EXISTS DamageReports ("
                        + "report_id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "equipment_id INT,"
                        + "reported_by INT,"
                        + "description TEXT,"
                        + "report_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                        + "status ENUM('reported', 'reviewed', 'resolved') DEFAULT 'reported',"
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                        + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                        + "FOREIGN KEY (equipment_id) REFERENCES Equipment(equipment_id),"
                        + "FOREIGN KEY (reported_by) REFERENCES Users(user_id)"
                        + ");";
                stmt.execute(createDamageReportsTable);

                // 創建WishList表
                String createWishListTable = "CREATE TABLE IF NOT EXISTS WishList ("
                        + "wish_id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "user_id INT,"
                        + "equipment_id INT,"
                        + "added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                        + "FOREIGN KEY (user_id) REFERENCES Users(user_id),"
                        + "FOREIGN KEY (equipment_id) REFERENCES Equipment(equipment_id)"
                        + ");";
                stmt.execute(createWishListTable);

                // 創建Deliveries表
                String createDeliveriesTable = "CREATE TABLE IF NOT EXISTS Deliveries ("
                        + "delivery_id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "booking_id INT,"
                        + "courier_id INT,"
                        + "pickup_location VARCHAR(255) NOT NULL,"
                        + "status ENUM('scheduled', 'in_transit', 'delivered') DEFAULT 'scheduled',"
                        + "scheduled_time TIMESTAMP,"
                        + "delivered_time TIMESTAMP NULL,"
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                        + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                        + "FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id),"
                        + "FOREIGN KEY (courier_id) REFERENCES Users(user_id)"
                        + ");";
                stmt.execute(createDeliveriesTable);

                System.out.println("All tables created successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
