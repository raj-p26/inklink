package com.example.inklink.dbhelpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.example.inklink.models.ReportedArticles
import com.example.inklink.parameters.ArticlesTableParams
import com.example.inklink.parameters.ReportedArticlesTableParams
import com.example.inklink.parameters.UserTableParams

import java.util.ArrayList

class ReportedArticleTableHelper(context: Context) :
    SQLiteOpenHelper(context, ReportedArticlesTableParams.DB_NAME, null, ReportedArticlesTableParams.DB_VERSION) {

    /**
     * Returns a list of `ReportedArticles` objects fetched from the database table.
     *
     * @return an `ArrayList` of `ReportedArticles` objects.
     */
    val allReportedArticles: ArrayList<ReportedArticles>
        get() {
            val reportedArticles = ArrayList<ReportedArticles>()
            val db = this.readableDatabase
            onCreate(db)

            val sql =
                """
                    SELECT COUNT(${ReportedArticlesTableParams.COLUMN_ARTICLE_ID}), ${ReportedArticlesTableParams.COLUMN_ARTICLE_ID}
                        FROM ${ReportedArticlesTableParams.TABLE_NAME} 
                        WHERE ${ReportedArticlesTableParams.COLUMN_REPORT_STATUS}=? 
                        GROUP BY ${ReportedArticlesTableParams.COLUMN_ARTICLE_ID}
                """
            val cursor = db.rawQuery(sql, arrayOf("unhandled"))

            while (cursor.moveToNext()) {
                val article = ReportedArticles()
                article.articleId = cursor.getInt(1)

                reportedArticles.add(article)
            }

            cursor.close()
            return reportedArticles
        }

    override fun onCreate(db: SQLiteDatabase) {
        val sql =
            """
                CREATE TABLE IF NOT EXISTS ${ReportedArticlesTableParams.TABLE_NAME} (
                    ${ReportedArticlesTableParams.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                    ${ReportedArticlesTableParams.COLUMN_ARTICLE_ID} INTEGER,
                    ${ReportedArticlesTableParams.COLUMN_USER_ID} INTEGER,
                    ${ReportedArticlesTableParams.COLUMN_REPORT_TYPE} TEXT,
                    ${ReportedArticlesTableParams.COLUMN_REPORT_STATUS} TEXT DEFAULT 'unhandled',
                    ${ReportedArticlesTableParams.COLUMN_REPORT_DATE} TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (${ReportedArticlesTableParams.COLUMN_USER_ID})
                    REFERENCES ${UserTableParams.TABLE_NAME}(${UserTableParams.COLUMN_ID})
                    ON UPDATE CASCADE ON DELETE CASCADE,
                    FOREIGN KEY (${ReportedArticlesTableParams.COLUMN_USER_ID})
                    REFERENCES ${ArticlesTableParams.TABLE_NAME}(${ArticlesTableParams.COLUMN_ID})
                    ON UPDATE CASCADE ON DELETE CASCADE
                )
            """

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE ${ReportedArticlesTableParams.TABLE_NAME}")

        onCreate(db)
    }

    /**
     * Adds report of provided reportedArticle in database table.
     *
     * @param article an object of `ReportedArticles` class which needs to be added in table.
     */
    fun addReport(article: ReportedArticles) {
        val db = this.writableDatabase
        val values = ContentValues()
        onCreate(db)

        values.put(ReportedArticlesTableParams.COLUMN_ARTICLE_ID, article.articleId)
        values.put(ReportedArticlesTableParams.COLUMN_USER_ID, article.userId)
        values.put(ReportedArticlesTableParams.COLUMN_REPORT_TYPE, article.reportType)

        db.insert(ReportedArticlesTableParams.TABLE_NAME, null, values)
    }

    /**
     * Returns an object of `ReportedArticles` if user has already reported the article.
     *
     * @param userId id of user who needs to be checked. userId must not be null.
     * @param articleId id of article which user has banned. articleId must not be null.
     * @return an object of `ReportedArticles` if user has reported the article.
     */
    fun checkUserReport(userId: Int, articleId: Int): ReportedArticles? {
        val db = this.writableDatabase
        onCreate(db)

        val articles = ReportedArticles()
        val sql =
            """
                SELECT * FROM ${ReportedArticlesTableParams.TABLE_NAME}
                    WHERE ${ReportedArticlesTableParams.COLUMN_USER_ID}=?
                    AND ${ReportedArticlesTableParams.COLUMN_ARTICLE_ID}=?
            """

        val cursor = db.rawQuery(
            sql,
            arrayOf(userId.toString(), articleId.toString())
        )

        if (cursor.moveToNext()) {
            articles.id = cursor.getInt(0)
            articles.articleId = cursor.getInt(1)
            articles.userId = cursor.getInt(2)
            articles.reportType = cursor.getString(3)
            articles.reportStatus = cursor.getString(4)

            return articles
        }

        cursor.close()
        return null
    }

    /**
     * Updates the status of provided reported article in the database.
     *
     * @param article object of `ReportedArticles` class whose status needs to be updated. article
     * must not be null.
     */
    fun updateReportStatus(article: ReportedArticles) {
        val db = this.writableDatabase
        onCreate(db)
        val values = ContentValues()

        values.put(ReportedArticlesTableParams.COLUMN_REPORT_STATUS, article.reportStatus)

        db.update(
            ReportedArticlesTableParams.TABLE_NAME,
            values,
            "${ReportedArticlesTableParams.COLUMN_ARTICLE_ID}=?",
            arrayOf(article.articleId.toString())
        )
    }
}
