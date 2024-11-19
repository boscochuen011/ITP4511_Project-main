<%-- 
    Document   : damageReportReview
    Created on : 2024年5月14日, 下午1:01:57
    Author     : boscochuen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="userInfo" class="ict.bean.UserInfo" scope="session"/>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="header.jsp"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Damage Report Review</title>
        <link href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css" rel="stylesheet">
        <link href="css/damageReportReview.css" rel="stylesheet">
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.7.12/umd/min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script src="js/damageReports.js"></script> 
    </head>

    <body>
        <h1>Damage Reports</h1>
        <div id="dataContainer">Loading data...</div> <!-- Data display area -->
        <div id="editStatusModal" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Edit Report Status</h4>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <label for="editStatus">New Status:</label>
                        <select id="editStatus" class="form-control">
                            <option value="reported">Reported</option>
                            <% if ("admin".equals(userInfo.getRole())) { %>
                                <option value="reviewed">Reviewed</option>
                            <% } %>
                            <option value="resolved">Resolved</option>
                        </select>
                    </div>
                    <div class="modal-footer">
                        <button id="saveEdit" type="button" class="btn btn-primary">Save Changes</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
