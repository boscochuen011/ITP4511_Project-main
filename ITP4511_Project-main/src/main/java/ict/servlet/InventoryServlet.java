/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet;

import ict.bean.EquipmentBean;
import ict.db.EquipmentDB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import java.io.StringReader;

@WebServlet("/InventoryServlet")
public class InventoryServlet extends HttpServlet {

    private EquipmentDB equipmentDB;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("MySQL JDBC driver not found", e);
        }
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");

        equipmentDB = new EquipmentDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("listAvailableJson".equals(action)) {
            listEquipmentJson(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void listEquipmentJson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<EquipmentBean> equipmentList = equipmentDB.getAllEquipment();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (EquipmentBean equipment : equipmentList) {
            System.out.println("Adding equipment to JSON: " + equipment.getName());
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("equipmentId", equipment.getEquipmentId())
                    .add("name", equipment.getName())
                    .add("description", equipment.getDescription())
                    .add("status", equipment.getStatus())
                    .add("location", equipment.getLocation())
                    .add("staffOnly", equipment.isStaffOnly())
            );
        }

        JsonArray jsonArray = jsonArrayBuilder.build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            updateEquipment(request, response);
        } else if ("upload".equals(action)) {
            request.getRequestDispatcher("/UploadServlet").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void updateEquipment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            JsonObject json = Json.createReader(new StringReader(sb.toString())).readObject();

            int equipmentId = json.getInt("equipmentId");
            String name = json.getString("name");
            String description = json.getString("description");
            String status = json.getString("status");
            String location = json.getString("location");
            boolean staffOnly = json.getBoolean("staffOnly");

            EquipmentBean equipment = new EquipmentBean();
            equipment.setEquipmentId(equipmentId);
            equipment.setName(name);
            equipment.setDescription(description);
            equipment.setStatus(status);
            equipment.setLocation(location);
            equipment.setStaffOnly(staffOnly);

            equipmentDB.addOrUpdateEquipment(equipment);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

}
