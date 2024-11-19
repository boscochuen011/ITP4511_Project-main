<%-- 
    Document   : loginError
    Created on : 2024年5月2日, 上午10:18:18
    Author     : puinamkwok
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p>InCorrect Password</p>
        <p>
            <% out.println("<a href=\"" + request.getContextPath() + "/main\">Login Again</a>"); %>
        </p>
    </body>
</html>
