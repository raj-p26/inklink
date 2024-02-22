package com.example.inklink.dbhelpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.example.inklink.models.User
import com.example.inklink.parameters.UserTableParams

import java.security.MessageDigest
import java.util.ArrayList
import kotlin.experimental.and

class UserTableHelper(context: Context) :
    SQLiteOpenHelper(context, UserTableParams.DB_NAME, null, UserTableParams.DB_VERSION) {
    private lateinit var myDb: SQLiteDatabase

    /**
     * Returns an array list of users, containing each field values inside a `User` object.
     *
     * @return an array list of users.
     */
    val users: ArrayList<User>
        get() {
            myDb = this.readableDatabase
            val sql =
                """
                    SELECT * FROM ${UserTableParams.TABLE_NAME} 
                        WHERE ${UserTableParams.COLUMN_EMAIL}!=? 
                        AND ${UserTableParams.COLUMN_ACCOUNT_STATUS}!=?
                """
            onCreate(myDb)
            val users = ArrayList<User>()
            val cursor = myDb.rawQuery(
                sql,
                arrayOf("admin@inklink.com", "suspended")
            )
            while (cursor.moveToNext()) {
                val user = User(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
                )
                user.id = cursor.getInt(0)
                user.username = cursor.getString(3)
                user.accountStatus = cursor.getString(7)
                user.registrationDate = cursor.getString(8)

                users.add(user)
            }

            cursor.close()
            return users
        }
    override fun onCreate(db: SQLiteDatabase) {
        val sql =
            """
                CREATE TABLE IF NOT EXISTS ${UserTableParams.TABLE_NAME} (
                    ${UserTableParams.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                    ${UserTableParams.COLUMN_FIRST_NAME} TEXT,
                    ${UserTableParams.COLUMN_LAST_NAME} TEXT,
                    ${UserTableParams.COLUMN_USER_NAME} TEXT,
                    ${UserTableParams.COLUMN_EMAIL} TEXT,
                    ${UserTableParams.COLUMN_PASSWORD} TEXT,
                    ${UserTableParams.COLUMN_ABOUT} TEXT DEFAULT 'A User in Ink Link',
                    ${UserTableParams.COLUMN_ACCOUNT_STATUS} TEXT DEFAULT 'active',
                    ${UserTableParams.COLUMN_REGISTRATION_DATE} TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE ${UserTableParams.TABLE_NAME}")
        onCreate(db)
    }

    /**
     * Adds a new user in the database table.
     * @param user the user that needs to be added. user must not be null.
     */
    fun addUser(user: User) {
        myDb = this.writableDatabase
        onCreate(myDb)
        val values = ContentValues()

        values.put(UserTableParams.COLUMN_FIRST_NAME, user.firstName)
        values.put(UserTableParams.COLUMN_LAST_NAME, user.lastName)
        values.put(UserTableParams.COLUMN_USER_NAME, user.firstName)
        values.put(UserTableParams.COLUMN_EMAIL, user.email)
        values.put(UserTableParams.COLUMN_PASSWORD, hashPassword(user.password))

        myDb.insert(UserTableParams.TABLE_NAME, null, values)
    }

    /**
     * Checks whether the user is accessing correct credentials or not.
     * The password of the user is hashed and not in plain text, so that no one can decode it.
     * If the user has entered correct credentials, it returns true, otherwise false.
     *
     * @param email user's email
     * @param password user's password
     * @return true if the user's credentials match in the database table.
     */
    fun getCredentials(email: String, password: String): Boolean {
        val query =
            """
                SELECT * FROM ${UserTableParams.TABLE_NAME} WHERE ${UserTableParams.COLUMN_EMAIL}=?
                AND ${UserTableParams.COLUMN_PASSWORD}=?
                AND ${UserTableParams.COLUMN_ACCOUNT_STATUS}<>?
            """
        myDb = this.readableDatabase
        onCreate(myDb)
        val cursor = myDb.rawQuery(
            query,
            arrayOf(email, hashPassword(password), "suspended")
        )
        val ret = cursor.moveToFirst()
        cursor.close()

        return ret
    }

    /**
     * Returns an object of `User` class if the id of user rests inside database table,
     * Otherwise it returns null.
     *
     * @param id user's id that needs to be fetched. id must not be null.
     * @return an object of `User` containing that specific user's values in table fields.
     */
    fun getUserById(id: Int): User? {
        val query =
            "SELECT * FROM ${UserTableParams.TABLE_NAME} WHERE ${UserTableParams.COLUMN_ID}=?"
        myDb = this.readableDatabase
        onCreate(myDb)
        val cursor = myDb.rawQuery(query, arrayOf(id.toString()))
        if (!cursor.moveToFirst()) {
            return null
        }

        val user = User()
        user.id = cursor.getInt(0)
        user.firstName = cursor.getString(1)
        user.lastName = cursor.getString(2)
        user.username = cursor.getString(3)
        user.email = cursor.getString(4)
        user.password = cursor.getString(5)
        user.about = cursor.getString(6)
        user.accountStatus = cursor.getString(7)
        user.registrationDate = cursor.getString(8)

        cursor.close()
        return user
    }

    /**
     * Returns an object of `User` class if the id of user rests inside database table,
     * Otherwise it returns null.
     *
     * @param email user's email that needs to be fetched. email must not be null.
     * @return an object of `User` containing that specific user's values in table fields.
     */
    fun getUserByEmail(email: String?): User? {
        val query =
            "SELECT * FROM ${UserTableParams.TABLE_NAME} WHERE ${UserTableParams.COLUMN_EMAIL}=?"
        myDb = this.readableDatabase
        onCreate(myDb)
        val cursor = myDb.rawQuery(query, arrayOf(email))

        if (!cursor.moveToFirst()) {
            return null
        }

        val user = User()
        user.id = cursor.getInt(0)
        user.firstName = cursor.getString(1)
        user.lastName = cursor.getString(2)
        user.username = cursor.getString(3)
        user.email = cursor.getString(4)
        user.password = cursor.getString(5)
        user.about = cursor.getString(6)
        user.accountStatus = cursor.getString(7)
        user.registrationDate = cursor.getString(8)

        cursor.close()
        return user
    }

    /**
     * Updates the fields of user table in the database.
     *
     * @param user a user that needs to be updated. user must not be null.
     */
    fun updateUser(user: User) {
        myDb = this.writableDatabase
        onCreate(myDb)
        val values = ContentValues()
        values.put(UserTableParams.COLUMN_FIRST_NAME, user.firstName)
        values.put(UserTableParams.COLUMN_LAST_NAME, user.lastName)
        values.put(UserTableParams.COLUMN_USER_NAME, user.username)
        values.put(UserTableParams.COLUMN_EMAIL, user.email)
        values.put(UserTableParams.COLUMN_ABOUT, user.about)
        if (!user.password!!.isBlank()) {
            values.put(UserTableParams.COLUMN_PASSWORD, hashPassword(user.password))
        }

        myDb.update(
            UserTableParams.TABLE_NAME,
            values,
            "${UserTableParams.COLUMN_ID}=?",
            arrayOf(user.id.toString())
        )
    }

    /**
     * [For Admin]
     * Sets status of account of the user to 'suspended' based on id.
     *
     * @param id the user's id who needs to be suspended. id must not be null.
     */
    fun suspendUser(id: Int) {
        myDb = this.writableDatabase
        onCreate(myDb)
        val values = ContentValues()

        values.put(UserTableParams.COLUMN_ACCOUNT_STATUS, "suspended")

        myDb.update(
            UserTableParams.TABLE_NAME,
            values,
            "${UserTableParams.COLUMN_ID}=?",
            arrayOf(id.toString())
        )
    }

    /**
     * Returns true if the user exists in the database table, Otherwise false.
     *
     * @param email the user's email to check if exists. email must not be null.
     * @return true if the user exists in table, else false.
     */
    fun exists(email: String): Boolean {
        return getUserByEmail(email) != null
    }

    /**
     * This method is used to get all of the suspended users in the 'users' table.
     * Returns list of 'Users' model.
     *
     * @return list of 'User' model.
     */
    fun getSuspendedUsers(): ArrayList<User> {
        myDb = readableDatabase
        val suspendedUsers: ArrayList<User> = ArrayList()
        val query = """
            SELECT ${UserTableParams.COLUMN_ID}, ${UserTableParams.COLUMN_USER_NAME} 
            FROM ${UserTableParams.TABLE_NAME} 
            WHERE ${UserTableParams.COLUMN_ACCOUNT_STATUS} = ?
        """
        val cursor: Cursor = myDb.rawQuery(
            query,
            arrayOf("suspended")
        )

        while (cursor.moveToNext()) {
            val user = User()
            user.id = cursor.getInt(0)
            user.username = cursor.getString(1)
            suspendedUsers.add(user)
        }

        cursor.close()
        return suspendedUsers
    }

    /**
     * This method is used to revert the suspension of the user.
     * 
     * @param userId the ID of the user whose suspension is to be reverted.
     */
    fun revertSuspension(userId: Int) {
        myDb = writableDatabase
        val values = ContentValues()
        values.put(UserTableParams.COLUMN_ACCOUNT_STATUS, "active")
        myDb.update(
            UserTableParams.TABLE_NAME,
            values,
            "${UserTableParams.COLUMN_ID}=?",
            arrayOf(userId.toString())
        )
    }

    /**
     * Hashes the provided plain text and converts it into number.
     * These cannot be decoded, so for checking it, the same plain text is given as argument
     * and the returned hash is compared.
     *
     * @param plainText the actual string that needs to be hashed.
     * @return hashed string of given password.
     */
    private fun hashPassword(plainText: String?): String {
        lateinit var generatedHash: String
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(plainText!!.toByteArray())

            val bytes = md.digest()

            val builder = StringBuilder()
            for (i in bytes) {
                builder
                    .append(((i and 0xff.toByte()) + 0x100)
                    .toString(16)
                    .substring(1))
            }

            generatedHash = builder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return generatedHash
    }
}
