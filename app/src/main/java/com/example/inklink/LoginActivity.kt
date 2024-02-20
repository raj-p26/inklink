package com.example.inklink

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.inklink.dbhelpers.UserTableHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userTableHelper: UserTableHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.login_button)
        emailInput = findViewById(R.id.login_email_input)
        passwordInput = findViewById(R.id.login_password_input)
        sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        userTableHelper = UserTableHelper(this)

        loginButton.setOnClickListener {
            if (validate()) startNewActivity()
        }
    }

    override fun onBackPressed() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validate(): Boolean {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        if (password.length < 8 || password.isEmpty()) {
            setError(passwordInput, "Password length should be at least 8 characters long")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(emailInput, "Entered email is not valid!")
            return false
        }

        if (!userTableHelper.getCredentials(emailInput.text.toString(), passwordInput.text.toString())) {
            showAlert()
            return false
        }

        return true
    }

    private fun setError(targetTextView: EditText, errorMessage: String) {
        targetTextView.error = errorMessage
        targetTextView.requestFocus()
    }

    private fun startNewActivity() {
        val intent: Intent
        val editor = sharedPreferences.edit()
        val user = UserTableHelper(this)
            .getUserByEmail(emailInput.text.toString())

        intent = if (user?.email == "admin@inklink.com")
            Intent(this, AdminActivity::class.java)
        else
            Intent(this, MainActivity::class.java)

        editor.putString("fname", user?.firstName)
        editor.putBoolean("isLoggedIn", true)
        editor.putString("email", emailInput.text.toString())
        editor.apply()

        startActivity(intent)
        finish()
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Invalid Credentials")
        builder.setMessage("email/password are invalid or Account has been suspended")
        builder.setPositiveButton("Ok", null)

        builder.create().show()
    }
}
