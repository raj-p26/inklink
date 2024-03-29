package com.example.inklink.models

/**
 * This class is a model for 'reported_users' table and contains all the columns of it.
 */
class ReportedUsers {
    var id: Int = 0
    var userId: Int = 0
    var reporterId: Int = 0
    var reportType: String? = null
    var reportStatus: String? = null

    init {
        this.reportStatus = "unhandled"
    }

    override fun toString(): String {
        return """
            ReportedUser: {
                id: ${this.id},
                userId: ${this.userId},
                reporterId: ${this.reporterId},
                reportType: ${this.reportType},
                reportStatus: ${this.reportStatus},
            }
        """.trimIndent()
    }
}
