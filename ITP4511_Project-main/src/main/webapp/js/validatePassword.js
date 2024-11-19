/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function validatePassword() {
    var password = document.getElementById("password");
    var confirm_password = document.getElementById("confirm_password");
    var submitBtn = document.getElementById("submitBtn");
    var message = document.getElementById("passwordMessage");

    if (password.value || confirm_password.value) { // Check if either password field has input
        if (password.value !== confirm_password.value) {
            confirm_password.setCustomValidity("Passwords do not match");
            password.classList.add("is-invalid");
            confirm_password.classList.add("is-invalid");
            message.textContent = "Passwords do not match"; // Display an error message
            submitBtn.disabled = true; // Disable the submit button if passwords do not match
        } else {
            confirm_password.setCustomValidity('');
            password.classList.remove("is-invalid");
            confirm_password.classList.remove("is-invalid");
            message.textContent = ''; // Clear the error message
            submitBtn.disabled = false; // Enable the submit button if passwords match
        }
    } else {
        submitBtn.disabled = false; // Ensure submit button is enabled if no passwords are entered
    }
}

window.onload = function () {
    var form = document.querySelector('form');
    document.getElementById("password").onchange = validatePassword;
    document.getElementById("confirm_password").onchange = validatePassword;
    document.getElementById("password").onkeyup = validatePassword;
    document.getElementById("confirm_password").onkeyup = validatePassword;
};
