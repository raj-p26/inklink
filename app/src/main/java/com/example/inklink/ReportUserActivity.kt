package com.example.inklink

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.inklink.dbhelpers.ReportedUserTableHelper
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.ReportedUsers

class ReportUserActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var reportButton: Button
    private lateinit var reportTypes: ArrayList<String>
    private var reportType: String? = null
    private lateinit var etEmail: EditText
    private lateinit var reportTypeSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_user)

        reportButton = findViewById(R.id.button)
        reportTypes = ArrayList()
        populateArrayList()

        etEmail = findViewById(R.id.ru_username)
        etEmail.setText(intent.getStringExtra("user_email"))

        reportTypeSpinner = findViewById(R.id.ru_spinner)

        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            reportTypes
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        reportTypeSpinner.adapter = arrayAdapter
        reportTypeSpinner.onItemSelectedListener = this

        reportButton.setOnClickListener {
            val helper = ReportedUserTableHelper(this)
            val userId = intent.getIntExtra("user_id", -1)
            val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val reporterId = UserTableHelper(this)
                .getUserByEmail(prefs.getString("email", null))
                ?.id

            val userReport = ReportedUsers()
            userReport.userId = userId
            userReport.reporterId = reporterId!!
            userReport.reportType = reportType

            helper.addUserReport(userReport)

            Toast.makeText(
                this,
                "User has been reported!",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        reportType = reportTypes[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun populateArrayList() {
        reportTypes.add("Harassment or Bullying")
        reportTypes.add("Spam or Misleading Content")
        reportTypes.add("Hate Speech or Discrimination")
        reportTypes.add("Inappropriate Content")
        reportTypes.add("Violence or Threats")
        reportTypes.add("Copyright or Plagiarism")
        reportTypes.add("Privacy Violation")
    }
}
