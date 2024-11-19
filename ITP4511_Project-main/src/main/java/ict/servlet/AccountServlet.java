/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet;

/**
 *
 * @author boscochuen
 */
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet("/AccountServlet")
@MultipartConfig
public class AccountServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ITP4511_Project";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            listAccounts(response);
        } else if ("get".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("user_id"));
            getAccount(userId, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            updateAccount(request, response);
        } else if ("create".equals(action)) {
            createAccount(request, response);
        } else if ("import".equals(action)) {
            importAccounts(request, response);
        }
    }

    private void listAccounts(HttpServletResponse response) throws IOException {
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Users")) {
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String json = convertListToJson(accounts);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    private void getAccount(int userId, HttpServletResponse response) throws IOException {
        Account account = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    account = new Account(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone_number")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String json = convertObjectToJson(account);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = Integer.parseInt(request.getParameter("user_id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone_number");
        String role = request.getParameter("role");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement("UPDATE Users SET username = ?, password = ?, first_name = ?, last_name = ?, email = ?, phone_number = ?, role = ? WHERE user_id = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, email);
            stmt.setString(6, phoneNumber);
            stmt.setString(7, role);
            stmt.setInt(8, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Error: " + e.getMessage());
            return;
        }
        response.setContentType("text/plain");
        response.getWriter().write("Success");
    }

    private void createAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone_number");
        String role = request.getParameter("role");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Users (username, password, first_name, last_name, email, phone_number, role) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, email);
            stmt.setString(6, phoneNumber);
            stmt.setString(7, role);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Error: " + e.getMessage());
            return;
        }
        response.setContentType("text/plain");
        response.getWriter().write("Success");
    }

    private void importAccounts(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part filePart = request.getPart("file");
        InputStream fileContent = filePart.getInputStream();

        try (Workbook workbook = new XSSFWorkbook(fileContent)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Skip header row
                    continue;
                }

                String username = row.getCell(0).getStringCellValue();
                String password = row.getCell(1).getStringCellValue();
                String role = row.getCell(2).getStringCellValue();
                String firstName = row.getCell(3).getStringCellValue();
                String lastName = row.getCell(4).getStringCellValue();
                String email = row.getCell(5).getStringCellValue();
                String phoneNumber = row.getCell(6).getStringCellValue();

                // Save to database
                saveAccountToDatabase(username, password, role, firstName, lastName, email, phoneNumber);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing file: " + e.getMessage());
            return;
        }

        response.setContentType("text/plain");
        response.getWriter().write("Accounts imported successfully");
    }

    private void saveAccountToDatabase(String username, String password, String role, String firstName, String lastName, String email, String phoneNumber) throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // 檢查電子郵件是否存在
            PreparedStatement checkEmailStmt = conn.prepareStatement("SELECT user_id FROM Users WHERE email = ?");
            checkEmailStmt.setString(1, email);
            ResultSet rsEmail = checkEmailStmt.executeQuery();
            if (rsEmail.next()) {
                // 電子郵件已存在，可以選擇跳過或更新
                System.out.println("Skipping existing email: " + email);
                // 或者更新現有用戶信息
                // Update existing user code here...
            } else {
                // 插入新用戶
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO Users (username, password, role, first_name, last_name, email, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)");
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, role);
                insertStmt.setString(4, firstName);
                insertStmt.setString(5, lastName);
                insertStmt.setString(6, email);
                insertStmt.setString(7, phoneNumber);
                insertStmt.executeUpdate();
            }
        }
    }

    private String convertListToJson(List<Account> accounts) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < accounts.size(); i++) {
            json.append(convertObjectToJson(accounts.get(i)));
            if (i < accounts.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    private String convertObjectToJson(Account account) {
        if (account == null) {
            return "{}";
        }
        return "{"
                + "\"user_id\":" + account.user_id + ","
                + "\"username\":\"" + account.username + "\","
                + "\"password\":\"" + account.password + "\","
                + "\"role\":\"" + account.role + "\","
                + "\"first_name\":\"" + account.first_name + "\","
                + "\"last_name\":\"" + account.last_name + "\","
                + "\"email\":\"" + account.email + "\","
                + "\"phone_number\":\"" + account.phone_number + "\""
                + "}";
    }

    private static class Account {

        private int user_id;
        private String username;
        private String password;
        private String role;
        private String first_name;
        private String last_name;
        private String email;
        private String phone_number;

        public Account(int user_id, String username, String password, String role, String first_name, String last_name, String email, String phone_number) {
            this.user_id = user_id;
            this.username = username;
            this.password = password;
            this.role = role;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
            this.phone_number = phone_number;
        }
    }
}
