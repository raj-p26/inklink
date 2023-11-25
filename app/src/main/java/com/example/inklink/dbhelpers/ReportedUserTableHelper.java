package com.example.inklink.dbhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inklink.models.ReportedUsers;
import com.example.inklink.parameters.ArticlesTableParams;
import com.example.inklink.parameters.UserTableParams;

import java.util.ArrayList;

import static com.example.inklink.parameters.ReportedUsersTableParams.COLUMN_ID;
import static com.example.inklink.parameters.ReportedUsersTableParams.COLUMN_REPORTER_ID;
import static com.example.inklink.parameters.ReportedUsersTableParams.COLUMN_REPORT_DATE;
import static com.example.inklink.parameters.ReportedUsersTableParams.COLUMN_REPORT_STATUS;
import static com.example.inklink.parameters.ReportedUsersTableParams.COLUMN_REPORT_TYPE;
import static com.example.inklink.parameters.ReportedUsersTableParams.COLUMN_USER_ID;
import static com.example.inklink.parameters.ReportedUsersTableParams.DB_NAME;
import static com.example.inklink.parameters.ReportedUsersTableParams.DB_VERSION;
import static com.example.inklink.parameters.ReportedUsersTableParams.TABLE_NAME;

public class ReportedUserTableHelper extends SQLiteOpenHelper {
    private Context context;

    public ReportedUserTableHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_REPORTER_ID + " INTEGER, " +
                COLUMN_REPORT_TYPE + " INTEGER, " +
                COLUMN_REPORT_STATUS + " TEXT DEFAULT 'unhandled', " +
                COLUMN_REPORT_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + COLUMN_USER_ID + ")" +
                "REFERENCES " + UserTableParams.TABLE_NAME +
                "(" + UserTableParams.COLUMN_ID + ")" +
                "ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY" +
                "("+ COLUMN_REPORTER_ID +")" +
                "REFERENCES "+ ArticlesTableParams.TABLE_NAME +
                "("+ ArticlesTableParams.COLUMN_ID +")" +
                "ON DELETE CASCADE ON UPDATE CASCADE)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);

        onCreate(db);
    }

    /**
     * Adds report of user in database table.
     *
     * @param user the object of `ReportedUsers` class which needs to be reported in database table.
     *             The user must not be null.
     */
    public void addUserReport(@NonNull ReportedUsers user) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_REPORTER_ID, user.getReporterId());
        values.put(COLUMN_REPORT_TYPE, user.getReportType());
        values.put(COLUMN_REPORT_STATUS, user.getReportStatus());

        db.insert(TABLE_NAME, null, values);
    }

    /**
     * Returns an `ArrayList` of `ReportedUsers` class object with values inside it.
     * If table is empty, null is returned.
     *
     * @return list of reported users in the database table.
     */
    public ArrayList<ReportedUsers> getAllReports() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ReportedUsers> list = new ArrayList<>();
        onCreate(db);
        String sql = "SELECT COUNT(" + COLUMN_USER_ID + "), " +
                COLUMN_USER_ID + " from " + TABLE_NAME + " WHERE " +
                COLUMN_REPORT_STATUS + "='unhandled' GROUP BY " + COLUMN_USER_ID;

        Cursor cursor = db.rawQuery(
            sql,
            null
        );

        while (cursor.moveToNext()) {
            ReportedUsers user = new ReportedUsers();
            user.setUserId(cursor.getInt(1));
            list.add(user);
        }

        cursor.close();
        return list;
    }


    /**
     * Returns an object of `ReportedUsers` class if the record exists with reportedId and
     * userId. If not then null is returned.
     *
     * @param reporterId the user's id who has reported a user. reporterId must not be null.
     * @param userId the user's id who has been reported. userId must not be null.
     * @return an object of `ReportedUsers` class if the record exists in table.
     */
    public ReportedUsers getReportByUserId(int reporterId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
        ReportedUsers user = new ReportedUsers();
        String sql = "SELECT * FROM " +
                TABLE_NAME + " WHERE " +
                COLUMN_USER_ID + "=? AND " +
                COLUMN_REPORTER_ID + "=?";
        Cursor cursor = db.rawQuery(
            sql,
            new String[] { String.valueOf(userId), String.valueOf(reporterId) }
        );

        if (!cursor.moveToNext())
            return null;

        user.setId(cursor.getInt(0));
        user.setUserId(cursor.getInt(1));
        user.setReporterId(cursor.getInt(2));
        user.setReportType(cursor.getString(3));
        user.setReportStatus(cursor.getString(4));

        cursor.close();
        return user;
    }

    /**
     * [For Admin]
     * updates the report status of provided reported user.
     *
     * @param user object of `ReportedUsers` class whose status needs to be updated. user must not
     *             be null.
     */
    public void updateReportStatus(@NonNull ReportedUsers user) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues values = new ContentValues();
        values.put(COLUMN_REPORT_STATUS, user.getReportStatus());

        db.update(
            TABLE_NAME,
            values,
            COLUMN_USER_ID + "=?",
            new String[] {String.valueOf(user.getUserId())}
        );
    }
}
