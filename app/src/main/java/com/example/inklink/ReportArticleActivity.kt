package com.example.inklink

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.inklink.dbhelpers.ArticleTableHelper
import com.example.inklink.dbhelpers.ReportedArticleTableHelper
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.Article
import com.example.inklink.models.ReportedArticles

class ReportArticleActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var reportType: String? = null
    private lateinit var reportTypeArray: ArrayList<String>
    private lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_article)

        val intent = intent
        val raArticleTitle: EditText = findViewById(R.id.ra_article_title)
        val raSpinner: Spinner = findViewById(R.id.spinner)
        val button: Button = findViewById(R.id.button)

        raArticleTitle.setText(intent.getStringExtra("article_title"))
        reportTypeArray = ArrayList()
        populateArrayList()
        prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            reportTypeArray
        )

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        raSpinner.adapter = arrayAdapter
        raSpinner.onItemSelectedListener = this

        button.setOnClickListener {
            val helper = ReportedArticleTableHelper(this)
            val reportedArticle = ReportedArticles()
            val article = Article()
            article.id = intent.getIntExtra("article_id", -1)
            val user = UserTableHelper(this)
                .getUserByEmail(prefs.getString("email", null))

            reportedArticle.reportType = reportType
            reportedArticle.articleId = intent.getIntExtra("article_id", -1)
            reportedArticle.userId = user.id

            helper.addReport(reportedArticle)

            // incremented article report count by 1
            ArticleTableHelper(this)
                .updateReportCount(article.id)

            showSuccessDialogAndExit()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        reportType = reportTypeArray[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun populateArrayList() {
        reportTypeArray.add("Religious Content")
        reportTypeArray.add("Violation")
        reportTypeArray.add("Harassment and Threats")
        reportTypeArray.add("Copyrighted Content")
        reportTypeArray.add("Spam")
        reportTypeArray.add("Malware and Viruses")
        reportTypeArray.add("Dangerous and Illegal Activities")
        reportTypeArray.add("Adult Content")
        reportTypeArray.add("Hate Speech")
        reportTypeArray.add("Violent Organization and Movements")
        reportTypeArray.add("Personal and Confidential Information")
        reportTypeArray.add("Impersonation and Misrepresentation of Identity")
    }

    private fun showSuccessDialogAndExit() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Report")
        builder.setMessage("This article has been reported!")
        builder.setPositiveButton("Ok") { _, _ -> finish() }

        builder.create().show()
    }
}
