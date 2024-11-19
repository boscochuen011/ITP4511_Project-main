/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet;

import ict.bean.EquipmentBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import ict.db.EquipmentDB;
import jakarta.servlet.RequestDispatcher;
import java.util.List;
/**
 *
 * @author puinamkwok
 */
@WebServlet("/equipment")
public class EquipmentServlet extends HttpServlet {
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
        
       String dbUser = this.getServletContext().getInitParameter("dbUser");
       String dbPassword = this.getServletContext().getInitParameter("dbPassword");
       String dbUrl = this.getServletContext().getInitParameter("dbUrl");
       equipmentDB = new EquipmentDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("listAvailable".equals(action)) {
            List<EquipmentBean> equipmentList = equipmentDB.getAllEquipment();
            request.setAttribute("equipmentList", equipmentList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("listEquipment.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }
}