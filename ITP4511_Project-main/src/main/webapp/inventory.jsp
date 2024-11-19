<%-- 
    Document   : inventory
    Created on : 2024年5月4日, 上午12:19:06
    Author     : boscochuen
--%>

<%@ page import="java.util.List" %>
<%@ page import="ict.bean.EquipmentBean" %>
<%@ page import="ict.db.EquipmentDB" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="userInfo" class="ict.bean.UserInfo" scope="session"/>
<jsp:useBean id="equipmentDB" class="ict.db.EquipmentDB" scope="application"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="header.jsp"/>
        <link href="css/inventory.css" rel="stylesheet">
        <script src="js/inventory.js"></script>
            <style>
        #reportModal {
            display: none; 
            position: fixed; 
            z-index: 1; 
            left: 0; 
            top: 0; 
            width: 100%; 
            height: 100%; 
            overflow: auto; 
            background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
        }
        #reportModalContent {
            background-color: #fefefe; 
            margin: 5% auto; 
            padding: 20px; 
            border: 1px solid #888; 
            width: 50%;
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
            border-radius: 10px;
        }
        .close {
            color: #aaa; 
            float: right; 
            font-size: 28px; 
            font-weight: bold;
            cursor: pointer;
        }
        .close:hover,
        .close:focus {
            color: #000;
            text-decoration: none;
            cursor: pointer;
        }
        #reportDamageForm {
            display: flex;
            flex-direction: column;
        }
        #reportDamageForm select,
        #reportDamageForm textarea,
        #reportDamageForm button {
            margin-top: 10px;
            padding: 10px;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        #reportDamageForm button {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        #reportDamageForm button:hover {
            background-color: #45a049;
        }
    </style>
    </head>
    <body>
        <div class="header">
            <h1>Inventory Data</h1>
            <button onclick="openReportModal()">Report Damages</button>

            <% if ("admin".equals(userInfo.getRole())) { %>
                <button onclick="window.location.href = 'damageReportReview.jsp';">Review Damage Reports</button>
                <button onclick="openUploadModal()">Upload Data</button>
            <% } %> 
            <div id="inventory"></div>
        </div>

        <div id="uploadModal" class="modal">
            <div class="modal-content">
                <span onclick="closeUploadModal()" style="color: #aaa; float: right; font-size: 28px; font-weight: bold;">&times;</span>
                <h2>Upload File</h2>
                <form id="uploadForm" enctype="multipart/form-data" method="post" action="UploadServlet">
                    <input type="file" id="fileInput" name="file" accept=".xlsx, .xls" required />
                    <button type="submit">Upload</button>
                </form>
            </div>
        </div>

        <!-- The Edit Modal -->
        <div id="editModal" style="display:none; position: fixed; z-index: 1; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgb(0,0,0); background-color: rgba(0,0,0,0.4);">
            <div style="background-color: #fefefe; margin: 5% auto; padding: 20px; border: 1px solid #888; width: 50%;">
                <span onclick="closeModal()" style="color: #aaa; float: right; font-size: 28px; font-weight: bold;">&times;</span>
                <p>Edit Equipment</p>
                <form id="editEquipmentForm">
                    <input type="hidden" id="editId">
                    Name: <input type="text" id="editName" <%= ("admin".equals(userInfo.getRole())) ? "" : "disabled" %>><br>
                    Description: <input type="text" id="editDescription" <%= ("admin".equals(userInfo.getRole())) ? "" : "disabled" %>><br>
                    Status:<br>
                    <select id="editStatus" <%= ("admin".equals(userInfo.getRole())) ? "" : "disabled" %>>
                        <option value="available">Available</option>
                        <option value="unavailable">Unavailable</option>
                        <option value="maintenance">Maintenance</option>
                        <option value="reserved">Reserved</option>
                    </select><br>
                    Location:<br>
                    <select id="editLocation" <%= ("admin".equals(userInfo.getRole())) ? "" : "disabled" %>>
                        <option value="Chai Wan">Chai Wan</option>
                        <option value="Tsing Yi">Tsing Yi</option>
                        <option value="Sha Tin">Sha Tin</option>
                        <option value="Tuen Mun">Tuen Mun</option>
                        <option value="Lee Wai Lee">Lee Wai Lee</option>
                    </select><br>
                    Staff Only: <input type="checkbox" id="editStaffOnly" <%= ("admin".equals(userInfo.getRole())) ? "" : "disabled" %>><br>
                    <button type="button" onclick="submitEdit()">Save Changes</button>
                </form>
            </div>
        </div>
                    
        <!-- The Report Damage Modal -->
        <div id="reportModal">
           <div id="reportModalContent">
               <span class="close" onclick="closeReportModal()">&times;</span>
               <p>Report Damage</p>
               <form id="reportDamageForm" method="post" action="ReportDamageServlet">
                   Equipment:
                   <select name="equipmentId" required>
                       <c:forEach var="equipment" items="${equipmentDB.getEquipmentNamesAndIds()}">
                           <option value="${equipment.equipmentId}">${equipment.name}</option>
                       </c:forEach>
                   </select><br>
                   <input type="hidden" name="reportedBy" value="${userInfo.userId}" required><br>
                   Description: <textarea name="description" required></textarea><br>
                   <button type="submit">Submit Report</button>
               </form>
           </div>
       </div>

        <script>
            function openReportModal() {
                document.getElementById("reportModal").style.display = "block";
            }

            function closeReportModal() {
                document.getElementById("reportModal").style.display = "none";
            }
        </script>
    </body>
</html>
