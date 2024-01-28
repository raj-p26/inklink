package com.example.inklink.models

class Article {
    var id: Int = 0
    var userId: Int = 0
    var reportCount: Int = 0
    var articleTitle: String? = null
    var articleContent: String? = null
    var articleStatus: String? = null
    var creationDate: String? = null

    constructor()

    constructor(title: String, content: String, userId: Int) {
        this.articleTitle = title
        this.articleContent = content
        this.userId = userId
    }

    override fun toString(): String {
        return """
            Article: {
                id: ${this.id},
                userId: ${this.userId},
                reportCount: ${this.reportCount},
                articleTitle: ${this.articleTitle},
                articleContent: ${this.articleContent},
                articleStatus: ${this.articleStatus},
                creationDate: ${this.creationDate},
            }
        """.trimIndent()
    }
}
