package com.example.inklink.dbhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.inklink.models.User;
import com.example.inklink.parameters.UserTableParams;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.util.ArrayList;

public class UserTableHelper extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase myDb;

    public UserTableHelper(Context context) {
        super(context, UserTableParams.DB_NAME, null, UserTableParams.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + UserTableParams.TABLE_NAME + "(" +
                UserTableParams.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UserTableParams.COLUMN_FIRST_NAME + " TEXT, " +
                UserTableParams.COLUMN_LAST_NAME + " TEXT, " +
                UserTableParams.COLUMN_USER_NAME + " TEXT, " +
                UserTableParams.COLUMN_EMAIL + " TEXT, " +
                UserTableParams.COLUMN_PASSWORD + " TEXT, " +
                UserTableParams.COLUMN_ABOUT + " TEXT DEFAULT 'A User in Ink Link', " +
                UserTableParams.COLUMN_ACCOUNT_STATUS + " TEXT DEFAULT 'active', " +
                UserTableParams.COLUMN_REGISTRATION_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + UserTableParams.TABLE_NAME);

        onCreate(db);
    }

    /**
     * Returns an array list of users, containing each field values inside a `User` object.
     *
     * @return an array list of users.
     */
    public ArrayList<User> getUsers() {
        myDb = this.getReadableDatabase();
        String sql = "SELECT * FROM " + UserTableParams.TABLE_NAME + " WHERE " +
                UserTableParams.COLUMN_EMAIL + "!=? AND " +
                UserTableParams.COLUMN_ACCOUNT_STATUS + "!=?";
        onCreate(myDb);
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = myDb.rawQuery(
            sql,
            new String[] { "admin@inklink.com", "suspended" }
        );
        while (cursor.moveToNext()) {
            User user = new User(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(3));
            user.setAccountStatus(cursor.getString(7));
            user.setRegistrationDate(cursor.getString(8));

            users.add(user);
        }

        cursor.close();
        return users;
    }

    /**
     * Adds a new user in the database table.
     *
     * @param user the user that needs to be added. user must not be null.
     */
    public void addUser(@NotNull User user) {
        myDb = this.getWritableDatabase();
        onCreate(myDb);
        ContentValues values = new ContentValues();

        values.put(UserTableParams.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(UserTableParams.COLUMN_LAST_NAME, user.getLastName());
        values.put(UserTableParams.COLUMN_USER_NAME, user.getFirstName());
        values.put(UserTableParams.COLUMN_EMAIL, user.getEmail());
        values.put(UserTableParams.COLUMN_PASSWORD, hashPassword(user.getPassword()));

        myDb.insert(UserTableParams.TABLE_NAME, null, values);
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
    public boolean getCredentials(String email, String password) {
        String query = "SELECT * FROM " + UserTableParams.TABLE_NAME + " WHERE " +
                UserTableParams.COLUMN_EMAIL + "=? AND " +
                UserTableParams.COLUMN_PASSWORD + "=? AND " +
                UserTableParams.COLUMN_ACCOUNT_STATUS + "<>?";
        myDb = this.getReadableDatabase();
        onCreate(myDb);
        Cursor cursor = myDb.rawQuery(
            query,
            new String[]{email, hashPassword(password), "suspended"}
        );
        boolean ret = cursor.moveToFirst();
        cursor.close();

        return ret;
    }

    /**
     * Returns an object of `User` class if the id of user rests inside database table,
     * Otherwise it returns null.
     *
     * @param id user's id that needs to be fetched. id must not be null.
     * @return an object of `User` containing that specific user's values in table fields.
     */
    public User getUserById(int id) {
        String query = "SELECT * FROM " + UserTableParams.TABLE_NAME + " WHERE " +
                UserTableParams.COLUMN_ID + "=?";
        myDb = this.getReadableDatabase();
        onCreate(myDb);
        Cursor cursor = myDb.rawQuery(query, new String[]{String.valueOf(id)});
        if (!cursor.moveToFirst()) {
            return null;
        }

        User user = new User();
        user.setId(cursor.getInt(0));
        user.setFirstName(cursor.getString(1));
        user.setLastName(cursor.getString(2));
        user.setUsername(cursor.getString(3));
        user.setEmail(cursor.getString(4));
        user.setPassword(cursor.getString(5));
        user.setAbout(cursor.getString(6));
        user.setAccountStatus(cursor.getString(7));
        user.setRegistrationDate(cursor.getString(8));

        cursor.close();
        return user;
    }

    /**
     * Returns an object of `User` class if the id of user rests inside database table,
     * Otherwise it returns null.
     *
     * @param email user's email that needs to be fetched. email must not be null.
     * @return an object of `User` containing that specific user's values in table fields.
     */
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM " + UserTableParams.TABLE_NAME + " WHERE " +
                UserTableParams.COLUMN_EMAIL + "=?";
        myDb = this.getReadableDatabase();
        onCreate(myDb);
        Cursor cursor = myDb.rawQuery(query, new String[]{email});

        if (!cursor.moveToFirst()) {
            return null;
        }

        User user = new User();
        user.setId(cursor.getInt(0));
        user.setFirstName(cursor.getString(1));
        user.setLastName(cursor.getString(2));
        user.setUsername(cursor.getString(3));
        user.setEmail(cursor.getString(4));
        user.setPassword(cursor.getString(5));
        user.setAbout(cursor.getString(6));
        user.setAccountStatus(cursor.getString(7));
        user.setRegistrationDate(cursor.getString(8));

        cursor.close();
        return user;
    }

    /**
     * Updates the fields of user table in the database.
     *
     * @param user a user that needs to be updated. user must not be null.
     */
    public void updateUser(@NotNull User user) {
        myDb = this.getWritableDatabase();
        onCreate(myDb);
        ContentValues values = new ContentValues();
        values.put(UserTableParams.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(UserTableParams.COLUMN_LAST_NAME, user.getLastName());
        values.put(UserTableParams.COLUMN_USER_NAME, user.getUsername());
        values.put(UserTableParams.COLUMN_EMAIL, user.getEmail());
        values.put(UserTableParams.COLUMN_PASSWORD, hashPassword(user.getPassword()));
        values.put(UserTableParams.COLUMN_ABOUT, user.getAbout());

        myDb.update(
            UserTableParams.TABLE_NAME,
            values,
            UserTableParams.COLUMN_ID + "=?",
            new String[]{String.valueOf(user.getId())}
        );
    }

    /**
     * [For Admin]
     * Sets status of account of the user to 'suspended' based on id.
     *
     * @param id the user's id who needs to be suspended. id must not be null.
     */
    public void suspendUser(int id) {
        myDb = this.getWritableDatabase();
        onCreate(myDb);
        ContentValues values = new ContentValues();

        values.put(UserTableParams.COLUMN_ACCOUNT_STATUS, "suspended");

        myDb.update(
            UserTableParams.TABLE_NAME,
            values,
            UserTableParams.COLUMN_ID + "=?",
            new String[]{String.valueOf(id)}
        );
    }

    /**
     * Returns true if the user exists in the database table, Otherwise false.
     *
     * @param email the user's email to check if exists. email must not be null.
     * @return true if the user exists in table, else false.
     */
    public boolean exists(String email) { return getUserByEmail(email) != null; }

    /**
     * Hashes the provided plain text and converts it into number.
     * These cannot be decoded, so for checking it, the same plain text is given as argument
     * and the returned hash is compared.
     *
     * @param plainText the actual string that needs to be hashed.
     * @return hashed string of given password.
     */
    private String hashPassword(String plainText) {
        String generatedHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());

            byte[] bytes = md.digest();

            StringBuilder builder = new StringBuilder();
            for (int i : bytes) {
                builder.append(Integer.toString((i & 0xff) + 0x100, 16).substring(1));
            }

            generatedHash = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedHash;
    }
}
