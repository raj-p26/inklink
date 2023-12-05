package com.example.inklink.models

class ReportedArticles {
    var id: Int? = 0
    var articleId: Int? = 0
    var userId: Int? = 0
    var reportType: String? = null
    var reportStatus: String? = null

    init {
        this.reportStatus = "unhandled"
    }
}
