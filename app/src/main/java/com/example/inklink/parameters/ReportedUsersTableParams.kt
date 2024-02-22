package com.example.inklink.parameters

/**
 * This object represents all the parameters for 'reported_users' table.
 */
object ReportedUsersTableParams {
    val DB_NAME = "ink_link"
    val DB_VERSION = 1
    val TABLE_NAME = "reported_users"

    val COLUMN_ID = "id"
    val COLUMN_USER_ID = "reported_user_id"
    val COLUMN_REPORTER_ID = "reporter_user_id"
    val COLUMN_REPORT_TYPE = "report_type"
    val COLUMN_REPORT_STATUS = "report_status"
    val COLUMN_REPORT_DATE = "report_date"
}
