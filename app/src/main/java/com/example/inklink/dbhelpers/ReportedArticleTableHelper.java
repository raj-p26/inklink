package com.example.inklink.dbhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.example.inklink.models.ReportedArticles;
import com.example.inklink.parameters.ArticlesTableParams;
import com.example.inklink.parameters.UserTableParams;

import java.util.ArrayList;

import static com.example.inklink.parameters.ReportedArticlesTableParams.COLUMN_ARTICLE_ID;
import static com.example.inklink.parameters.ReportedArticlesTableParams.COLUMN_ID;
import static com.example.inklink.parameters.ReportedArticlesTableParams.COLUMN_REPORT_DATE;
import static com.example.inklink.parameters.ReportedArticlesTableParams.COLUMN_REPORT_STATUS;
import static com.example.inklink.parameters.ReportedArticlesTableParams.COLUMN_REPORT_TYPE;
import static com.example.inklink.parameters.ReportedArticlesTableParams.COLUMN_USER_ID;
import static com.example.inklink.parameters.ReportedArticlesTableParams.DB_NAME;
import static com.example.inklink.parameters.ReportedArticlesTableParams.DB_VERSION;
import static com.example.inklink.parameters.ReportedArticlesTableParams.TABLE_NAME;

public class ReportedArticleTableHelper extends SQLiteOpenHelper {
    private Context context;

    public ReportedArticleTableHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ARTICLE_ID + " INTEGER, " +
            COLUMN_USER_ID + " INTEGER, " +
            COLUMN_REPORT_TYPE + " TEXT, " +
            COLUMN_REPORT_STATUS + " TEXT DEFAULT 'unhandled', " +
            COLUMN_REPORT_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " +
            UserTableParams.TABLE_NAME +
            "(" + UserTableParams.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, " +
            "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " +
            ArticlesTableParams.TABLE_NAME +
            "(" + ArticlesTableParams.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE" +
            ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ TABLE_NAME);

        onCreate(db);
    }

    /**
     * Adds report of provided reportedArticle in database table.
     *
     * @param article an object of `ReportedArticles` class which needs to be added in table.
     */
    public void addReport(@NonNull ReportedArticles article) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        onCreate(db);

        values.put(COLUMN_ARTICLE_ID, article.getArticleId());
        values.put(COLUMN_USER_ID, article.getUserId());
        values.put(COLUMN_REPORT_TYPE, article.getReportType());

        db.insert(TABLE_NAME, null, values);
    }

    /**
     * Returns a list of `ReportedArticles` objects fetched from the database table.
     *
     * @return an `ArrayList` of `ReportedArticles` objects.
     */
    public ArrayList<ReportedArticles> getAllReportedArticles() {
        ArrayList<ReportedArticles> reportedArticles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);

        String sql = "SELECT COUNT(" + COLUMN_ARTICLE_ID + "), " +
                COLUMN_ARTICLE_ID + " from " + TABLE_NAME + " WHERE " +
                COLUMN_REPORT_STATUS + "=? GROUP BY " + COLUMN_ARTICLE_ID;
        Cursor cursor = db.rawQuery(sql, new String[] { "unhandled" });

        while (cursor.moveToNext()) {
            ReportedArticles article = new ReportedArticles();
            article.setArticleId(cursor.getInt(1));

            reportedArticles.add(article);
        }

        cursor.close();
        return reportedArticles;
    }

    /**
     * Returns an object of `ReportedArticles` if user has already reported the article.
     *
     * @param userId id of user who needs to be checked. userId must not be null.
     * @param articleId id of article which user has banned. articleId must not be null.
     * @return an object of `ReportedArticles` if user has reported the article.
     */
    public ReportedArticles checkUserReport(int userId, int articleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);

        ReportedArticles articles = new ReportedArticles();
        String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USER_ID + "=? AND " + COLUMN_ARTICLE_ID + "=?";

        Cursor cursor = db.rawQuery(
            sql,
            new String[] {
                String.valueOf(userId),
                String.valueOf(articleId)
            }
        );

        if (cursor.moveToNext()) {
            articles.setId(cursor.getInt(0));
            articles.setArticleId(cursor.getInt(1));
            articles.setUserId(cursor.getInt(2));
            articles.setReportType(cursor.getString(3));
            articles.setReportStatus(cursor.getString(4));

            return articles;
        }

        cursor.close();
        return null;
    }

    /**
     * Updates the status of provided reported article in the database.
     *
     * @param article object of `ReportedArticles` class whose status needs to be updated. article
     *                must not be null.
     */
    public void updateReportStatus(@NonNull ReportedArticles article) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues values = new ContentValues();

        values.put(COLUMN_REPORT_STATUS, article.getReportStatus());

        db.update(
            TABLE_NAME,
            values,
            COLUMN_ARTICLE_ID + "=?",
            new String[] { String.valueOf(article.getArticleId()) }
        );
    }
}
