package com.example.inklink

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.User

class RegisterActivity : AppCompatActivity() {
    private lateinit var fnameInput: EditText
    private lateinit var lnameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fnameInput = findViewById(R.id.sign_up_input_first_name)
        lnameInput = findViewById(R.id.sign_up_input_last_name)
        emailInput = findViewById(R.id.sign_up_input_email)
        passwordInput = findViewById(R.id.sign_up_input_password)
        confirmPasswordInput = findViewById(R.id.sign_up_input_cpassword)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val userTableHelper = UserTableHelper(this@RegisterActivity)
            if(validateData()) {
                val user = User(
                    fnameInput.text.toString(),
                    lnameInput.text.toString(),
                    emailInput.text.toString(),
                    passwordInput.text.toString(),
                    "User at Ink Link"
                )
                sharedPrefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                val intent = Intent(this, MainActivity::class.java)

                editor.putString("fname", fnameInput.text.toString())
                editor.putString("email", emailInput.text.toString())
                editor.putBoolean("isLoggedIn" ,true)
                editor.apply()

                userTableHelper.addUser(user)

                startActivity(intent)
                finish()
            }
        }
    }

    private fun validateData(): Boolean {
        val fname = fnameInput.text.toString()
        val lname = lnameInput.text.toString()
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        val cpassword = confirmPasswordInput.text.toString()
        val helper = UserTableHelper(this)

        if (fname.isEmpty()) {
            showError(fnameInput, "First name cannot be blank")
            return false
        }

        if (lname.isEmpty()) {
            showError(lnameInput, "Last name cannot be blank")
            return false
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(emailInput, "Invalid email address")
            return false
        }

        if (password.isEmpty() || password.length < 8) {
            showError(passwordInput, "Password must be 8 characters long")
            return false
        }

        if (cpassword.isEmpty() || cpassword != password) {
            showError(confirmPasswordInput, "Passwords did not match!")
            return false
        }

        if (helper.exists(email)) {
            showError(emailInput, "Email is already taken!")
            return false
        }

        return true
    }

    private fun showError(targetView: TextView, msg: String) {
        targetView.error = msg
        targetView.requestFocus()
    }
}
