package com.example.inklink.parameters

/**
 * This object represents all the parameters of the 'articles' table.
 */
object ArticlesTableParams {
    val DB_NAME = "ink_link"
    val DB_VERSION = 1
    val TABLE_NAME = "articles"

    val COLUMN_ID = "article_id"
    val COLUMN_TITLE = "article_title"
    val COLUMN_CONTENT = "article_content"
    val COLUMN_USER_ID = "user_id"
    val COLUMN_STATUS = "article_status"
    val COLUMN_REPORT_COUNT = "report_count"
    val COLUMN_CREATION_DATE = "article_creation_date"
}
