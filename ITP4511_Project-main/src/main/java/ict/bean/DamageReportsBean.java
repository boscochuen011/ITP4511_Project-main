/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.bean;

import java.sql.Timestamp;

/**
 *
 * @author puinamkwok
 */
public class DamageReportsBean {
    private int reportId;
    private int equipmentId;
    private int reportedBy;
    private String description;
    private Timestamp reportDate;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    public DamageReportsBean() {}

    // Constructor with all fields
    public DamageReportsBean(int reportId, int equipmentId, int reportedBy, String description, Timestamp reportDate, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.reportId = reportId;
        this.equipmentId = equipmentId;
        this.reportedBy = reportedBy;
        this.description = description;
        this.reportDate = reportDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(int reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getReportDate() {
        return reportDate;
    }

    public void setReportDate(Timestamp reportDate) {
        this.reportDate = reportDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
