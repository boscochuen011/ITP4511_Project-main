/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.bean;

import java.io.Serializable;
import java.sql.*;
import org.json.JSONObject;
/**
 *
 * @author puinamkwok
 */
public class UserInfo implements Serializable {

    private int userId;
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public UserInfo() {
    }

    public UserInfo(String username, String password, String role, String firstName, String lastName, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = new Timestamp(System.currentTimeMillis()); // Defaults to current time
        this.updatedAt = new Timestamp(System.currentTimeMillis()); // Defaults to current time
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("username", username);
        json.put("password", password);
        json.put("role", role);
        json.put("firstName", firstName);
        json.put("lastName", lastName);
        json.put("email", email);
        json.put("phoneNumber", phoneNumber);
        return json.toString();
    }

    // Getter 和 Setter 方法
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
