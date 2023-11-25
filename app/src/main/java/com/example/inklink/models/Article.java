package com.example.inklink.models;

public class Article {
    private int id, userId, reportCount;
    private String articleTitle, articleContent, articleStatus, creationDate;

    public Article() {}

    public Article(String title, String content, int userId) {
        this.articleTitle = title;
        this.articleContent = content;
        this.userId = userId;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public int getReportCount() { return reportCount; }

    public void setReportCount(int reportCount) { this.reportCount = reportCount; }

    public String getArticleTitle() { return articleTitle; }

    public void setArticleTitle(String articleTitle) { this.articleTitle = articleTitle; }

    public String getArticleContent() { return articleContent; }

    public void setArticleContent(String articleContent) { this.articleContent = articleContent; }

    public String getArticleStatus() { return articleStatus; }

    public void setArticleStatus(String articleStatus) { this.articleStatus = articleStatus; }

    public String getCreationDate() { return creationDate; }

    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }
}
