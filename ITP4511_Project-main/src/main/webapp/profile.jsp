<%-- 
    Document   : profile
    Created on : 2024年5月2日, 下午10:39:54
    Author     : boscochuen
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Update User</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/profile.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
    <jsp:useBean id="userInfo" class="ict.bean.UserInfo" scope="session"/>
    <jsp:include page="header.jsp"/>
    <div class="container">
        <h2>Update User Information</h2>
        <form id="updateUserForm" class="needs-validation" novalidate>
            <input type="hidden" name="userId" value="${userInfo.userId}">
            <div class="form-group">
                <label for="role">Role:</label>
                <input type="text" name="role" class="form-control" id="role" value="${userInfo.role}" disabled>
            </div>
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" name="username" class="form-control" id="username" value="${userInfo.username}">
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" name="password" class="form-control" id="password" required autocomplete="new-password">
                <small id="passwordMessage" class="text-danger"></small>
            </div>
            <div class="form-group">
                <label for="confirm_password">Confirm Password:</label>
                <input type="password" name="confirm_password" class="form-control" id="confirm_password" required autocomplete="new-password">
            </div>
            <div class="form-group">
                <label for="firstName">First Name:</label>
                <input type="text" name="firstName" class="form-control" id="firstName" value="${userInfo.firstName}">
            </div>
            <div class="form-group">
                <label for="lastName">Last Name:</label>
                <input type="text" name="lastName" class="form-control" id="lastName" value="${userInfo.lastName}">
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" name="email" class="form-control" id="email" value="${userInfo.email}">
            </div>
            <div class="form-group">
                <label for="phoneNumber">Phone Number:</label>
                <input type="text" name="phoneNumber" class="form-control" id="phoneNumber" value="${userInfo.phoneNumber}">
            </div>
            <button type="submit" class="btn btn-primary" id="submitBtn">Update User</button>
        </form>
        <div id="updateMessage" class="mt-3"></div>
    </div>

    <script>
        $(document).ready(function () {
            $('#updateUserForm').on('submit', function (event) {
                event.preventDefault();
                var password = $('#password').val();
                var confirm_password = $('#confirm_password').val();
                if (password !== confirm_password) {
                    $('#passwordMessage').text('Passwords do not match.');
                    $('#updateMessage').html('<div class="alert alert-danger">Update failed: Passwords do not match.</div>');
                    return;
                } else {
                    $('#passwordMessage').text('');
                }
                var formData = $(this).serialize();
                $.ajax({
                    url: 'UserServlet',
                    type: 'POST',
                    data: formData,
                    dataType: 'json',
                    success: function (userInfo) {
                        console.log("Update successful: ", userInfo);
                        $('#username').val(userInfo.username);
                        $('#firstName').val(userInfo.firstName);
                        $('#lastName').val(userInfo.lastName);
                        $('#email').val(userInfo.email);
                        $('#phoneNumber').val(userInfo.phoneNumber);
                        $('#updateMessage').html('<div class="alert alert-success">User information updated successfully.</div>');
                    },
                    error: function (xhr, status, error) {
                        console.error("Update failed: ", xhr.responseText);
                        $('#updateMessage').html('<div class="alert alert-danger">An error occurred. Please try again.</div>');
                    }
                });
            });
        });
    </script>
</body>
</html>
