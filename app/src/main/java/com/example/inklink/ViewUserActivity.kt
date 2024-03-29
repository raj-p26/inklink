package com.example.inklink

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.inklink.dbhelpers.ReportedUserTableHelper
import com.example.inklink.dbhelpers.UserTableHelper

class ViewUserActivity : AppCompatActivity() {

    private lateinit var reportUserButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user)
        val intent = intent

        supportActionBar?.title = "About: ${intent.getStringExtra("user_username")}"

        val fName: TextView = findViewById(R.id.vu_profile_edit_name)
        val lName: TextView = findViewById(R.id.vu_profile_edit_last_name)
        val username: TextView = findViewById(R.id.vu_profile_edit_username)
        val email: TextView = findViewById(R.id.vu_profile_edit_email)
        val about: TextView = findViewById(R.id.vu_profile_edit_about)

        fName.text = intent.getStringExtra("user_first_name")
        lName.text = intent.getStringExtra("user_last_name")
        username.text = intent.getStringExtra("user_username")
        email.text = intent.getStringExtra("user_email")
        about.text = intent.getStringExtra("user_about")
        reportUserButton = findViewById(R.id.vu_report_btn)

        toggleReportButton()

        if (!canReport()) {
            hideReportButton()
        }

        reportUserButton.setOnClickListener {
            val openRUActivity = Intent(this, ReportUserActivity::class.java)
            openRUActivity.putExtra(
                "user_id",
                this.intent.getIntExtra("user_id", -1)
            )

            openRUActivity.putExtra(
                "user_email",
                this.intent.getStringExtra("user_email")
            )

            startActivityForResult(openRUActivity, 1)
        }

        val user = UserTableHelper(this)
            .getUserById(intent.getIntExtra("user_id", -1))

        if (user!!.accountStatus == "suspended") {
            showDialogAndExit()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            recreate()
        }
    }

    /**
     * This method is used to check whether the user can report or not.
     * If the user is admin or the user himself/herself, they cannot report.
     * If the user has already reported this user, they also cannot report.
     * Otherwise they can report.
     *
     * @return true if the user can report, otherwise false.
     */
    private fun canReport(): Boolean {
        val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        if (prefs.getString("email", null).equals(null))
            return false

        val user = UserTableHelper(this)
            .getUserByEmail(prefs.getString("email", null))
        val helper = ReportedUserTableHelper(this)
        val vuUserId = intent.getIntExtra("user_id", -1)

        return helper.getReportByUserId(user!!.id, vuUserId) == null
    }

    /**
     * This method is used to toggle the report button, This method uses another helper method
     * to hide the button.
     */
    private fun toggleReportButton() {
        val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        if (prefs.getString("email", null).equals(null)) {
            hideReportButton()
        }
    }

    /**
     * This method is used to show the alert dialog and exit the activity.
     */
    private fun showDialogAndExit() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Warning")
        builder.setMessage("This user has been suspended by the administrator!")
        builder.setPositiveButton("Ok") { _, _ -> finish() }
        builder.setCancelable(false)

        builder.create().show()
    }

    /**
     * This method is used to hide the report button.
     */
    private fun hideReportButton() {
        reportUserButton.visibility = View.GONE
        reportUserButton.isEnabled = false
    }
}
