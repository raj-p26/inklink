package com.example.inklink.models;

public class ReportedArticles {
    private int id, articleId, userId;
    private String reportType, reportStatus;

    public ReportedArticles() {
        this.reportStatus = "unhandled";
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getArticleId() { return articleId; }

    public void setArticleId(int articleId) { this.articleId = articleId; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getReportType() { return reportType; }

    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getReportStatus() { return reportStatus; }

    public void setReportStatus(String reportStatus) { this.reportStatus = reportStatus; }
}
