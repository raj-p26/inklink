package com.example.inklink.dbhelpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.example.inklink.models.ReportedUsers
import com.example.inklink.parameters.ArticlesTableParams
import com.example.inklink.parameters.ReportedUsersTableParams
import com.example.inklink.parameters.UserTableParams

import java.util.ArrayList

class ReportedUserTableHelper(private val context: Context?) : SQLiteOpenHelper(
    context,
    ReportedUsersTableParams.DB_NAME,
    null,
    ReportedUsersTableParams.DB_VERSION
) {

    /**
     * Returns an `ArrayList` of `ReportedUsers` class object with values inside it.
     * If table is empty, null is returned.
     *
     * @return list of reported users in the database table.
     */
    val allReports: ArrayList<ReportedUsers>
        get() {
            val db = this.readableDatabase
            val list = ArrayList<ReportedUsers>()
            onCreate(db)
            val sql = "SELECT COUNT(" + ReportedUsersTableParams.COLUMN_USER_ID + "), " +
                    ReportedUsersTableParams.COLUMN_USER_ID + " from " + ReportedUsersTableParams.TABLE_NAME + " WHERE " +
                    ReportedUsersTableParams.COLUMN_REPORT_STATUS + "='unhandled' GROUP BY " + ReportedUsersTableParams.COLUMN_USER_ID

            val cursor = db.rawQuery(
                sql, null
            )

            while (cursor.moveToNext()) {
                val user = ReportedUsers()
                user.userId = cursor.getInt(1)
                list.add(user)
            }

            cursor.close()
            return list
        }

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "CREATE TABLE IF NOT EXISTS " + ReportedUsersTableParams.TABLE_NAME + "(" +
                ReportedUsersTableParams.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReportedUsersTableParams.COLUMN_USER_ID + " INTEGER, " +
                ReportedUsersTableParams.COLUMN_REPORTER_ID + " INTEGER, " +
                ReportedUsersTableParams.COLUMN_REPORT_TYPE + " INTEGER, " +
                ReportedUsersTableParams.COLUMN_REPORT_STATUS + " TEXT DEFAULT 'unhandled', " +
                ReportedUsersTableParams.COLUMN_REPORT_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + ReportedUsersTableParams.COLUMN_USER_ID + ")" +
                "REFERENCES " + UserTableParams.TABLE_NAME +
                "(" + UserTableParams.COLUMN_ID + ")" +
                "ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY" +
                "(" + ReportedUsersTableParams.COLUMN_REPORTER_ID + ")" +
                "REFERENCES " + ArticlesTableParams.TABLE_NAME +
                "(" + ArticlesTableParams.COLUMN_ID + ")" +
                "ON DELETE CASCADE ON UPDATE CASCADE)"

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE " + ReportedUsersTableParams.TABLE_NAME)

        onCreate(db)
    }

    /**
     * Adds report of user in database table.
     *
     * @param user the object of `ReportedUsers` class which needs to be reported in database table.
     * The user must not be null.
     */
    fun addUserReport(user: ReportedUsers) {
        val db = this.writableDatabase
        onCreate(db)
        val values = ContentValues()

        values.put(ReportedUsersTableParams.COLUMN_USER_ID, user.userId)
        values.put(ReportedUsersTableParams.COLUMN_REPORTER_ID, user.reporterId)
        values.put(ReportedUsersTableParams.COLUMN_REPORT_TYPE, user.reportType)
        values.put(ReportedUsersTableParams.COLUMN_REPORT_STATUS, user.reportStatus)

        db.insert(ReportedUsersTableParams.TABLE_NAME, null, values)
    }


    /**
     * Returns an object of `ReportedUsers` class if the record exists with reportedId and
     * userId. If not then null is returned.
     *
     * @param reporterId the user's id who has reported a user. reporterId must not be null.
     * @param userId the user's id who has been reported. userId must not be null.
     * @return an object of `ReportedUsers` class if the record exists in table.
     */
    fun getReportByUserId(reporterId: Int, userId: Int): ReportedUsers? {
        val db = this.readableDatabase
        onCreate(db)
        val user = ReportedUsers()
        val sql = "SELECT * FROM " +
                ReportedUsersTableParams.TABLE_NAME + " WHERE " +
                ReportedUsersTableParams.COLUMN_USER_ID + "=? AND " +
                ReportedUsersTableParams.COLUMN_REPORTER_ID + "=?"
        val cursor = db.rawQuery(
            sql,
            arrayOf(userId.toString(), reporterId.toString())
        )

        if (!cursor.moveToNext())
            return null

        user.id = cursor.getInt(0)
        user.userId = cursor.getInt(1)
        user.reporterId = cursor.getInt(2)
        user.reportType = cursor.getString(3)
        user.reportStatus = cursor.getString(4)

        cursor.close()
        return user
    }

    /**
     * [For Admin]
     * updates the report status of provided reported user.
     *
     * @param user object of `ReportedUsers` class whose status needs to be updated. user must not
     * be null.
     */
    fun updateReportStatus(user: ReportedUsers) {
        val db = this.writableDatabase
        onCreate(db)
        val values = ContentValues()
        values.put(ReportedUsersTableParams.COLUMN_REPORT_STATUS, user.reportStatus)

        db.update(
            ReportedUsersTableParams.TABLE_NAME,
            values,
            ReportedUsersTableParams.COLUMN_USER_ID + "=?",
            arrayOf(user.userId.toString())
        )
    }
}
