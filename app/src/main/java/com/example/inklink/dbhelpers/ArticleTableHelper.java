package com.example.inklink.dbhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inklink.models.Article;
import com.example.inklink.parameters.UserTableParams;

import java.util.ArrayList;

import static com.example.inklink.parameters.ArticlesTableParams.COLUMN_CONTENT;
import static com.example.inklink.parameters.ArticlesTableParams.COLUMN_CREATION_DATE;
import static com.example.inklink.parameters.ArticlesTableParams.COLUMN_ID;
import static com.example.inklink.parameters.ArticlesTableParams.COLUMN_REPORT_COUNT;
import static com.example.inklink.parameters.ArticlesTableParams.COLUMN_STATUS;
import static com.example.inklink.parameters.ArticlesTableParams.COLUMN_TITLE;
import static com.example.inklink.parameters.ArticlesTableParams.COLUMN_USER_ID;
import static com.example.inklink.parameters.ArticlesTableParams.DB_NAME;
import static com.example.inklink.parameters.ArticlesTableParams.DB_VERSION;
import static com.example.inklink.parameters.ArticlesTableParams.TABLE_NAME;

public class ArticleTableHelper extends SQLiteOpenHelper {
    public ArticleTableHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT," +
                COLUMN_CONTENT + " TEXT," +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_STATUS + " TEXT DEFAULT 'draft'," +
                COLUMN_REPORT_COUNT + " INTEGER DEFAULT 0," +
                COLUMN_CREATION_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY " +
                "(" + COLUMN_USER_ID + ") " +
                "REFERENCES " + UserTableParams.TABLE_NAME +
                "(" + UserTableParams.COLUMN_ID + ") ON DELETE CASCADE ON DELETE CASCADE)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);

        onCreate(db);
    }

    /**
     * Adds an article in the database
     *
     * @param article The article you want to add in the database. The article must not be null.
     */
    public void addArticle(@NonNull Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, article.getArticleTitle());
        values.put(COLUMN_CONTENT, article.getArticleContent());
        values.put(COLUMN_USER_ID, article.getUserId());
        values.put(COLUMN_STATUS, article.getArticleStatus());

        db.insert(TABLE_NAME, null, values);
    }

    /**
     * Returns an `ArrayList` of `Article` containing all articles inside database table
     *
     * @return an array list of all articles including banned articles
     */
    public ArrayList<Article> getAllArticles() {
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
        ArrayList<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_STATUS + "=? OR " + COLUMN_STATUS + "=?";
        Cursor cursor = db.rawQuery(sql, new String[] { "published", "banned" });

        while (cursor.moveToNext()) {
            Article article = new Article();
            article.setId(cursor.getInt(0));
            article.setArticleTitle(cursor.getString(1));
            article.setArticleContent(cursor.getString(2));
            article.setUserId(cursor.getInt(3));
            article.setArticleStatus(cursor.getString(4));
            article.setReportCount(cursor.getInt(5));
            article.setCreationDate(cursor.getString(6));

            articles.add(article);
        }

        cursor.close();
        return articles;
    }

    /**
     * Returns an `ArrayList` of `Article` containing all published articles
     *
     * @return an array list of published articles
     */
    public ArrayList<Article> getPublishedArticles() {
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
        ArrayList<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_STATUS + "=?";
        Cursor cursor = db.rawQuery(sql, new String[] { "published" });

        while (cursor.moveToNext()) {
            Article article = new Article();
            article.setId(cursor.getInt(0));
            article.setArticleTitle(cursor.getString(1));
            article.setArticleContent(cursor.getString(2));
            article.setUserId(cursor.getInt(3));
            article.setArticleStatus(cursor.getString(4));
            article.setReportCount(cursor.getInt(5));
            article.setCreationDate(cursor.getString(6));

            articles.add(article);
        }

        cursor.close();
        return articles;
    }

    /**
     * Returns an `ArrayList` of `Article` containing top 10 newly published articles.
     *
     * @return an array list of latest articles
     */
    public ArrayList<Article> getLatestArticles() {
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
        ArrayList<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_STATUS + "=? ORDER BY " + COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(sql, new String[] { "published" });

        while (cursor.moveToNext()) {
            Article article = new Article();
            article.setId(cursor.getInt(0));
            article.setArticleTitle(cursor.getString(1));
            article.setArticleContent(cursor.getString(2));
            article.setUserId(cursor.getInt(3));
            article.setArticleStatus(cursor.getString(4));
            article.setReportCount(cursor.getInt(5));
            article.setCreationDate(cursor.getString(6));

            articles.add(article);
        }

        cursor.close();
        return articles;
    }

    /**
     * Fetches articles of provided user id and returns an `ArrayList`
     *
     * @param userId The id of user. id must not be null.
     * @return an array list of a specific user's articles.
     */
    public ArrayList<Article> getUserSpecificArticles(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
        ArrayList<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + "=?";

        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(userId) });

        while (cursor.moveToNext()) {
            Article article = new Article();
            article.setId(cursor.getInt(0));
            article.setArticleTitle(cursor.getString(1));
            article.setArticleContent(cursor.getString(2));
            article.setUserId(cursor.getInt(3));
            article.setArticleStatus(cursor.getString(4));
            article.setReportCount(cursor.getInt(5));
            article.setCreationDate(cursor.getString(6));

            articles.add(article);
        }

        cursor.close();
        return articles;
    }

    /**
     * Fetches article of provided id and returns a new object of `Article`
     *
     * @param id The id of article. id must not be null.
     * @return an object of `Article` containing all field values.
     */
    public Article getArticle(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(id) });

        cursor.moveToNext();
        Article article = new Article();
        article.setId(cursor.getInt(0));
        article.setArticleTitle(cursor.getString(1));
        article.setArticleContent(cursor.getString(2));
        article.setUserId(cursor.getInt(3));
        article.setArticleStatus(cursor.getString(4));
        article.setReportCount(cursor.getInt(5));

        cursor.close();
        return article;
    }

    /**
     * First fetches an article based on the provided id and then
     * updates it's report count by 1.
     *
     * @param articleId id of article whose report count has to increase.
     */
    public void updateReportCount(int articleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        Article article = getArticle(articleId);
        ContentValues values = new ContentValues();

        values.put(COLUMN_REPORT_COUNT, (article.getReportCount() + 1));

        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[] { String.valueOf(articleId) });
    }

    /**
     * Updates the status of article by provided `Article` object.
     * The article status can be 'draft'(if user has edited),
     * 'published'(if user has edited and published at same time) or
     * 'banned'(if the admin finds out flaws and bans the article content)
     *
     * @param article article whose status needs to be updated. article must not be null
     */
    public void updateArticleStatus(@NonNull Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, article.getArticleTitle());
        values.put(COLUMN_CONTENT, article.getArticleContent());
        values.put(COLUMN_STATUS, article.getArticleStatus());

        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[] { String.valueOf(article.getId()) });
    }

    /**
     * Deletes the article from database only if the status of the article is 'draft'.
     *
     * @param article the `Article` object that needs to be deleted. The article must not be null
     */
    public void deleteDraftArticle(@NonNull Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        db.delete(TABLE_NAME,
            COLUMN_ID + "=?",
            new String[] { String.valueOf(article.getId()) }
        );
    }
}
