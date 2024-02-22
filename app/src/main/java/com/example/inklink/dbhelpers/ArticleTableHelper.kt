package com.example.inklink.dbhelpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.example.inklink.models.Article
import com.example.inklink.parameters.ArticlesTableParams
import com.example.inklink.parameters.UserTableParams

import java.util.ArrayList

class ArticleTableHelper(context: Context?) :
    SQLiteOpenHelper(context, UserTableParams.DB_NAME, null, UserTableParams.DB_VERSION) {

    /**
     * Returns an `ArrayList` of `Article` containing all articles inside database table
     *
     * @return an array list of all articles including banned articles
     */
    val allArticles: ArrayList<Article>
        get() {
            val db = this.readableDatabase
            onCreate(db)
            val articles = ArrayList<Article>()
            val sql =
                """
                    SELECT * FROM ${ArticlesTableParams.TABLE_NAME}
                    WHERE ${ArticlesTableParams.COLUMN_STATUS}=? 
                    OR ${ArticlesTableParams.COLUMN_STATUS}=?
                """
            val cursor = db.rawQuery(sql, arrayOf("published", "banned"))

            while (cursor.moveToNext()) {
                val article = Article()
                article.id = cursor.getInt(0)
                article.articleTitle = cursor.getString(1)
                article.articleContent = cursor.getString(2)
                article.userId = cursor.getInt(3)
                article.articleStatus = cursor.getString(4)
                article.reportCount = cursor.getInt(5)
                article.creationDate = cursor.getString(6)

                articles.add(article)
            }

            cursor.close()
            return articles
        }

    /**
     * Returns an `ArrayList` of `Article` containing all published articles
     *
     * @return an array list of published articles
     */
    val publishedArticles: ArrayList<Article>
        get() {
            val db = this.readableDatabase
            onCreate(db)
            val articles = ArrayList<Article>()
            val sql =
                """SELECT * FROM ${ArticlesTableParams.TABLE_NAME} 
                    WHERE ${ArticlesTableParams.COLUMN_STATUS}=?
                """
            val cursor = db.rawQuery(sql, arrayOf("published"))

            while (cursor.moveToNext()) {
                val article = Article()
                article.id = cursor.getInt(0)
                article.articleTitle = cursor.getString(1)
                article.articleContent = cursor.getString(2)
                article.userId = cursor.getInt(3)
                article.articleStatus = cursor.getString(4)
                article.reportCount = cursor.getInt(5)
                article.creationDate = cursor.getString(6)

                articles.add(article)
            }

            cursor.close()
            return articles
        }

    /**
     * Returns an `ArrayList` of `Article` containing top 10 newly published articles.
     *
     * @return an array list of latest articles
     */
    val latestArticles: ArrayList<Article>
        get() {
            val db = this.readableDatabase
            onCreate(db)
            val articles = ArrayList<Article>()
            val sql =
                """
                    SELECT * FROM ${ArticlesTableParams.TABLE_NAME}
                        WHERE ${ArticlesTableParams.COLUMN_STATUS}=?
                        ORDER BY ${ArticlesTableParams.COLUMN_ID} DESC
                """

            val cursor = db.rawQuery(sql, arrayOf("published"))

            while (cursor.moveToNext()) {
                val article = Article()
                article.id = cursor.getInt(0)
                article.articleTitle = cursor.getString(1)
                article.articleContent = cursor.getString(2)
                article.userId = cursor.getInt(3)
                article.articleStatus = cursor.getString(4)
                article.reportCount = cursor.getInt(5)
                article.creationDate = cursor.getString(6)

                articles.add(article)
            }

            cursor.close()
            return articles
        }

    override fun onCreate(db: SQLiteDatabase) {
        val sql =
            """
                CREATE TABLE IF NOT EXISTS ${ArticlesTableParams.TABLE_NAME} (
                    ${ArticlesTableParams.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                    ${ArticlesTableParams.COLUMN_TITLE} TEXT,
                    ${ArticlesTableParams.COLUMN_CONTENT} TEXT,
                    ${ArticlesTableParams.COLUMN_USER_ID} INTEGER,
                    ${ArticlesTableParams.COLUMN_STATUS} TEXT DEFAULT 'draft',
                    ${ArticlesTableParams.COLUMN_REPORT_COUNT} INTEGER DEFAULT 0,
                    ${ArticlesTableParams.COLUMN_CREATION_DATE} TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (${ArticlesTableParams.COLUMN_USER_ID})
                    REFERENCES ${UserTableParams.TABLE_NAME}(${UserTableParams.COLUMN_ID}) 
                    ON DELETE CASCADE ON DELETE CASCADE
                )
            """

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE ${ArticlesTableParams.TABLE_NAME}")

        onCreate(db)
    }

    /**
     * Adds an article in the database
     *
     * @param article The article you want to add in the database. The article must not be null.
     */
    fun addArticle(article: Article) {
        val db = this.writableDatabase
        onCreate(db)
        val values = ContentValues()

        values.put(ArticlesTableParams.COLUMN_TITLE, article.articleTitle)
        values.put(ArticlesTableParams.COLUMN_CONTENT, article.articleContent)
        values.put(ArticlesTableParams.COLUMN_USER_ID, article.userId)
        values.put(ArticlesTableParams.COLUMN_STATUS, article.articleStatus)

        db.insert(ArticlesTableParams.TABLE_NAME, null, values)
    }

    /**
     * Fetches articles of provided user id and returns an `ArrayList`
     *
     * @param userId The id of user. id must not be null.
     * @return an array list of a specific user's articles.
     */
    fun getUserSpecificArticles(userId: Int?): ArrayList<Article> {
        val db = this.readableDatabase
        onCreate(db)
        val articles = ArrayList<Article>()
        val sql =
            """SELECT * FROM ${ArticlesTableParams.TABLE_NAME}
                    WHERE ${ArticlesTableParams.COLUMN_USER_ID}=?"""

        val cursor = db.rawQuery(sql, arrayOf(userId.toString()))

        while (cursor.moveToNext()) {
            val article = Article()
            article.id = cursor.getInt(0)
            article.articleTitle = cursor.getString(1)
            article.articleContent = cursor.getString(2)
            article.userId = cursor.getInt(3)
            article.articleStatus = cursor.getString(4)
            article.reportCount = cursor.getInt(5)
            article.creationDate = cursor.getString(6)

            articles.add(article)
        }

        cursor.close()
        return articles
    }

    /**
     * Fetches article of provided id and returns a new object of `Article`
     *
     * @param id The id of article. id must not be null.
     * @return an object of `Article` containing all field values.
     */
    fun getArticle(id: Int?): Article {
        val db = this.readableDatabase
        onCreate(db)
        val sql =
            """SELECT * FROM ${ArticlesTableParams.TABLE_NAME}
                WHERE ${ArticlesTableParams.COLUMN_ID}=?"""
        val cursor = db.rawQuery(sql, arrayOf(id.toString()))

        cursor.moveToNext()
        val article = Article()
        article.id = cursor.getInt(0)
        article.articleTitle = cursor.getString(1)
        article.articleContent = cursor.getString(2)
        article.userId = cursor.getInt(3)
        article.articleStatus = cursor.getString(4)
        article.reportCount = cursor.getInt(5)

        cursor.close()
        return article
    }

    /**
     * First fetches an article based on the provided id and then
     * updates it's report count by 1.
     *
     * @param articleId id of article whose report count has to increase.
     */
    fun updateReportCount(articleId: Int) {
        val db = this.writableDatabase
        onCreate(db)
        val article = getArticle(articleId)
        val values = ContentValues()

        values.put(ArticlesTableParams.COLUMN_REPORT_COUNT, article.reportCount + 1)

        db.update(
            ArticlesTableParams.TABLE_NAME,
            values,
            ArticlesTableParams.COLUMN_ID + "=?",
            arrayOf(articleId.toString())
        )
    }

    /**
     * Updates the status of article by provided `Article` object.
     * The article status can be 'draft'(if user has edited),
     * 'published'(if user has edited and published at same time) or
     * 'banned'(if the admin finds out flaws and bans the article content)
     *
     * @param article article whose status needs to be updated. article must not be null
     */
    fun updateArticleStatus(article: Article) {
        val db = this.writableDatabase
        onCreate(db)
        val values = ContentValues()

        values.put(ArticlesTableParams.COLUMN_TITLE, article.articleTitle)
        values.put(ArticlesTableParams.COLUMN_CONTENT, article.articleContent)
        values.put(ArticlesTableParams.COLUMN_STATUS, article.articleStatus)

        db.update(
            ArticlesTableParams.TABLE_NAME,
            values,
            ArticlesTableParams.COLUMN_ID + "=?",
            arrayOf(article.id.toString())
        )
    }

    /**
     * Deletes the article from database only if the status of the article is 'draft'.
     *
     * @param article the `Article` object that needs to be deleted. The article must not be null
     */
    fun deleteDraftArticle(article: Article) {
        val db = this.writableDatabase
        onCreate(db)
        db.delete(
            ArticlesTableParams.TABLE_NAME,
            ArticlesTableParams.COLUMN_ID + "=?",
            arrayOf(article.id.toString())
        )
    }

    /**
     * This method is used to get all the banned articles from the table.
     *
     * @return array list of 'Article' model.
     */
    fun getBannedArticles(): ArrayList<Article> {
        val bannedArticles: ArrayList<Article> = ArrayList()
        val db = this.readableDatabase
        val query =
            """SELECT ${ArticlesTableParams.COLUMN_ID}, ${ArticlesTableParams.COLUMN_TITLE}
                FROM ${ArticlesTableParams.TABLE_NAME}
                WHERE ${ArticlesTableParams.COLUMN_STATUS} = ?
            """
        val cursor = db.rawQuery(query, arrayOf("banned"))

        while (cursor.moveToNext()) {
            val article = Article()
            article.id = cursor.getInt(0)
            article.articleTitle = cursor.getString(1)

            bannedArticles.add(article)
        }
        cursor.close()
        return bannedArticles
    }

    /**
     * This method is used to unban the banned article from the table.
     *
     * @param articleId the ID of the article which has to unban.
     */
    fun unbanArticle(articleId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ArticlesTableParams.COLUMN_STATUS, "published")
        db!!.update(
            ArticlesTableParams.TABLE_NAME,
            values,
            "${ArticlesTableParams.COLUMN_ID}=?",
            arrayOf(articleId.toString())
        )
    }
}
