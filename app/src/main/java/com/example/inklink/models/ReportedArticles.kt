package com.example.inklink.models

/**
 * This class is a model for 'reported_articles' table and contains all the columns of it.
 */
class ReportedArticles {
    var id: Int? = 0
    var articleId: Int? = 0
    var userId: Int? = 0
    var reportType: String? = null
    var reportStatus: String? = null

    init {
        this.reportStatus = "unhandled"
    }

    override fun toString(): String {
        return """
            ReportedArticle: {
                id: ${this.id},
                articleId: ${this.articleId},
                userId: ${this.userId},
                reportType: ${this.reportType},
                reportStatus: ${this.reportStatus},
            }
        """.trimIndent()
    }
}
