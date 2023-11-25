package com.example.inklink.models;

public class ReportedUsers {
    private int id, userId, reporterId;
    private String reportType, reportStatus;

    public ReportedUsers () {
        this.reportStatus = "unhandled";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setReporterId(int reporterId) { this.reporterId = reporterId; }

    public int getReporterId() { return reporterId; }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }
}
