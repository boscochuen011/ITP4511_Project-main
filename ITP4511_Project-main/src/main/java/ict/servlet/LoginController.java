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
import java.io.IOException;
import ict.bean.UserInfo;
import ict.db.UserDB;
import jakarta.servlet.RequestDispatcher;

/**
 *
 * @author puinamkwok
 */
@WebServlet("/main")
public class LoginController extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (!isAuthenticated(request) && !"authenticate".equals(action)) {
            doLogin(request, response);
            return;
        }
        switch (action) {
            case "authenticate":
                doAuthenticate(request, response);
                break;
            case "logout":
                doLogout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
                break;
        }
    }

    private void doAuthenticate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String targetURL;
        UserInfo userInfo = db.getUserByUsernameAndPassword(username, password);
        if (userInfo != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("userInfo", userInfo);
            session.setAttribute("userId", userInfo.getUserId());
            targetURL = "main.jsp";
        } else {
            targetURL = "loginError.jsp";
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/" + targetURL);
        rd.forward(request, response);
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        boolean result = false;
        HttpSession session = request.getSession();
        //get the UserInfo from session
        if (session.getAttribute("userInfo") != null) {
            result = true;
        }
        return result;
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String targetURL = "login.jsp";
        RequestDispatcher rd;
        rd = getServletContext().getRequestDispatcher("/" + targetURL);
        rd.forward(request, response);
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("userInfo");
            session.invalidate();
        }
        doLogin(request, response);
    }
}