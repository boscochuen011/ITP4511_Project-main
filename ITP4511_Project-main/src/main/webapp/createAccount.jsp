<%-- 
    Document   : createAccount
    Created on : 2024?5?16?, ??11:46:14
    Author     : boscochuen
--%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="header.jsp"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Account Management</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h1>Account Management</h1>
        <button class="btn btn-success mb-3" id="createAccountButton">Create Account</button>
        <button class="btn btn-primary mb-3" id="importAccountsButton">Import Accounts</button>
        <form id="uploadForm" enctype="multipart/form-data" style="display:none;">
            <input type="file" name="file" id="fileInput" accept=".xlsx, .xls">
        </form>
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Phone Number</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody id="accountTableBody">
                <%-- Account rows will be populated by AJAX --%>
            </tbody>
        </table>
    </div>

    <!-- Create/Edit Modal -->
    <div class="modal fade" id="accountModal" tabindex="-1" role="dialog" aria-labelledby="accountModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="accountModalLabel">Create/Edit Account</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form id="accountForm">
                        <input type="hidden" id="userId" name="user_id">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="form-group">
                            <label for="firstName">First Name</label>
                            <input type="text" class="form-control" id="firstName" name="first_name">
                        </div>
                        <div class="form-group">
                            <label for="lastName">Last Name</label>
                            <input type="text" class="form-control" id="lastName" name="last_name">
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" class="form-control" id="email" name="email">
                        </div>
                        <div class="form-group">
                            <label for="phoneNumber">Phone Number</label>
                            <input type="text" class="form-control" id="phoneNumber" name="phone_number">
                        </div>
                        <div class="form-group">
                            <label for="role">Role</label>
                            <select class="form-control" id="role" name="role" required>
                                <option value="user">User</option>
                                <option value="staff">Staff</option>
                                <option value="technician">Technician</option>
                                <option value="admin">Admin</option>
                                <option value="courier">Courier</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="saveChangesButton">Save changes</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>

    <script>
        $(document).ready(function () {
            // Fetch and display accounts
            $.ajax({
                url: 'AccountServlet?action=list',
                method: 'GET',
                dataType: 'json',
                success: function (data) {
                    var accountTableBody = $('#accountTableBody');
                    accountTableBody.empty();
                    $.each(data, function (index, account) {
                        accountTableBody.append(
                                '<tr>' +
                                '<td>' + account.user_id + '</td>' +
                                '<td>' + account.username + '</td>' +
                                '<td>' + account.first_name + '</td>' +
                                '<td>' + account.last_name + '</td>' +
                                '<td>' + account.email + '</td>' +
                                '<td>' + account.phone_number + '</td>' +
                                '<td>' + account.role + '</td>' +
                                '<td><button class="btn btn-primary editButton" data-id="' + account.user_id + '">Edit</button></td>' +
                                '</tr>'
                                );
                    });
                }
            });

            $(document).on('click', '.close, .btn-secondary', function () {
                $('#accountModal').modal('hide');
            });

            // Handle create account button click
            $('#createAccountButton').click(function () {
                $('#accountForm')[0].reset();
                $('#userId').val('');
                $('#accountModalLabel').text('Create Account');
                $('#saveChangesButton').off('click').click(createAccount);
                $('#accountModal').modal('show');
            });

            // Handle import accounts button click
            $('#importAccountsButton').click(function () {
                $('#fileInput').click();
            });

            // Handle file input change
            $('#fileInput').change(function () {
                var formData = new FormData($('#uploadForm')[0]);
                $.ajax({
                    url: 'AccountServlet?action=import',
                    method: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        alert('Accounts imported successfully');
                        location.reload(); // Reload the page to reflect the changes
                    },
                    error: function (xhr, status, error) {
                        alert('Error: ' + xhr.responseText);
                    }
                });
            });

            // Handle edit button click
            $(document).on('click', '.editButton', function () {
                var userId = $(this).data('id');
                $.ajax({
                    url: 'AccountServlet?action=get&user_id=' + userId,
                    method: 'GET',
                    dataType: 'json',
                    success: function (account) {
                        $('#userId').val(account.user_id);
                        $('#username').val(account.username);
                        $('#password').val(''); // Password should not be pre-filled for security reasons
                        $('#firstName').val(account.first_name);
                        $('#lastName').val(account.last_name);
                        $('#email').val(account.email);
                        $('#phoneNumber').val(account.phone_number);
                        $('#role').val(account.role);
                        $('#accountModalLabel').text('Edit Account');
                        $('#saveChangesButton').off('click').click(updateAccount);
                        $('#accountModal').modal('show');
                    }
                });
            });

            // Handle save changes button click
            function createAccount() {
                $.ajax({
                    url: 'AccountServlet?action=create',
                    method: 'POST',
                    data: $('#accountForm').serialize(),
                    success: function (response) {
                        $('#accountModal').modal('hide');
                        location.reload(); // Reload the page to reflect the changes
                    },
                    error: function (xhr, status, error) {
                        alert('Error: ' + xhr.responseText);
                    }
                });
            }

            function updateAccount() {
                $.ajax({
                    url: 'AccountServlet?action=update',
                    method: 'POST',
                    data: $('#accountForm').serialize(),
                    success: function (response) {
                        $('#accountModal').modal('hide');
                        location.reload(); // Reload the page to reflect the changes
                    },
                    error: function (xhr, status, error) {
                        alert('Error: ' + xhr.responseText);
                    }
                });
            }
        });
    </script>
</body>
</html>
