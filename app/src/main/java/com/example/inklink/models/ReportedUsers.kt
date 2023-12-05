package com.example.inklink.models

class ReportedUsers {
    var id: Int = 0
    var userId: Int = 0
    var reporterId: Int = 0
    var reportType: String? = null
    var reportStatus: String? = null

    init {
        this.reportStatus = "unhandled"
    }
}
