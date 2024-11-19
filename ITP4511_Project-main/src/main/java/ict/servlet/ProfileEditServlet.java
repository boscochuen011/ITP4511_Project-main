/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ict.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ict.bean.UserInfo;
import ict.db.UserDB;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

/**
 *
 * @author boscochuen
 */

@WebServlet("/UserServlet")
public class ProfileEditServlet extends HttpServlet {
    private UserDB db;

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
        db = new UserDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Integer sessionUserId = (Integer) session.getAttribute("userId"); // Assuming 'userId' is stored as an Integer

        if (sessionUserId == null) {
            response.sendRedirect("login.jsp"); // Redirect to login if session doesn't have userId
            return;
        }

        // Code to update user details if needed
        String username = checkInput(request.getParameter("username"));
        String password = checkInput(request.getParameter("password"));
        String firstName = checkInput(request.getParameter("firstName"));
        String lastName = checkInput(request.getParameter("lastName"));
        String email = checkInput(request.getParameter("email"));
        String phoneNumber = checkInput(request.getParameter("phoneNumber"));
        String role = checkInput(request.getParameter("role"));

        UserInfo userInfo = new UserInfo(username, password, role, firstName, lastName, email, phoneNumber);
        userInfo.setUserId(sessionUserId);

        try {
            boolean isUpdated = db.updateUser(userInfo);
            if (isUpdated) {
                // Retrieve updated user details from database
                UserInfo updatedUser = db.getUserById(sessionUserId);

                // Update session with the new user information
                session.setAttribute("userInfo", updatedUser);

                // Return the updated user information as JSON
                JSONObject json = new JSONObject();
                json.put("username", updatedUser.getUsername());
                json.put("firstName", updatedUser.getFirstName());
                json.put("lastName", updatedUser.getLastName());
                json.put("email", updatedUser.getEmail());
                json.put("phoneNumber", updatedUser.getPhoneNumber());

                out.write(json.toString());
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"message\":\"There was a problem updating the record.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\":\"Error updating record: " + e.getMessage() + "\"}");
            e.printStackTrace(System.out);  // Print stack trace to the console
        }
    }

    private String checkInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;  // Return null if input is empty or just white space
        }
        return input.trim();
    }
}
