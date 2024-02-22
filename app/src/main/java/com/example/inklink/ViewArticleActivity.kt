package com.example.inklink

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.inklink.dbhelpers.ArticleTableHelper
import com.example.inklink.dbhelpers.ReportedArticleTableHelper
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.Article

class ViewArticleActivity : AppCompatActivity() {

    private lateinit var vaAuthor: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_article)
        val intent = intent
        supportActionBar?.title = intent.getStringExtra("article_title")

        val vaContent: TextView = findViewById(R.id.va_content)
        vaAuthor = findViewById(R.id.va_author_name)

        vaContent.text = intent.getStringExtra("article_content")

        vaAuthor.text = "Created By: ${intent.getStringExtra("article_author")}"

        if (isBanned()) {
            vaContent.visibility = View.GONE
            showDialogAndExit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val intent = intent

        if (authenticate()) {
            vaAuthor.visibility = View.GONE
            if (intent.getStringExtra("article_status").equals("draft")) {
                menu?.add(Menu.NONE, R.integer.va_edit_opt_id, Menu.NONE, "Edit Article")
                menu?.add(Menu.NONE, R.integer.va_delete_opt_id, Menu.NONE, "Delete Article")
            }
        } else {
            if (canReport())
                menu?.add(Menu.NONE, R.integer.va_report_opt_id, Menu.NONE, "Report Article")
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val getIntent = intent
        when (item.itemId) {
            R.integer.va_edit_opt_id -> {
                val intent = Intent(this, CreateArticleActivity::class.java)
                intent.putExtra(
                    "article_content",
                    getIntent.getStringExtra("article_content")
                )

                intent.putExtra(
                    "article_title",
                    getIntent.getStringExtra("article_title")
                )

                intent.putExtra(
                    "article_id",
                    getIntent.getIntExtra("article_id", -1)
                )

                intent.putExtra("is_draft", true)

                startActivity(intent)
                finish()
            }

            R.integer.va_delete_opt_id -> {
                val helper = ArticleTableHelper(this)
                val article = Article()
                article.id = getIntent.getIntExtra("article_id", -1)

                helper.deleteDraftArticle(article)
                Toast.makeText(
                    this,
                    "Article has been deleted!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            R.integer.va_report_opt_id -> {
                val intent = Intent(this, ReportArticleActivity::class.java)
                intent.putExtra(
                    "article_title",
                    getIntent.getStringExtra("article_title")
                )
                intent.putExtra(
                    "article_id",
                    getIntent.getIntExtra("article_id", -1)
                )

                this.startActivityForResult(intent, 1)
            }
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) recreate()
    }

    /**
     * This method is used to check whether the user is admin or author of the article.
     *
     * @return true if user is either admin or author of the article otherwise false.
     */
    private fun authenticate(): Boolean {
        val intent = intent
        val user = UserTableHelper(this)
            .getUserById(intent.getIntExtra("author_id", -1))
        val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

        val isAdmin = prefs
            .getString("email", null)
            .equals("admin@inklink.com")

        if (user?.email == prefs.getString("email", null) ||
            isAdmin) {
            return true
        }

        return false
    }

    /**
     * This method is used to check whether the article is banned or not.
     *
     * @return true if the article is banned else false.
     */
    private fun isBanned(): Boolean {
        return intent.getStringExtra("article_status").equals("banned")
    }

    /**
     * This method shows alert dialog and finishes the activity.
     */
    private fun showDialogAndExit() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Warning")
        builder.setMessage("The content of this article has been banned by The admin")
        builder.setPositiveButton("Ok") { _, _ -> finish() }
        builder.setCancelable(false)

        builder.show()
    }

    /**
     * This method is used to check whether the user can report this article or not.
     * If the user is admin or author of the article, he/she cannot report his/her own article.
     * If the user is neither admin nor author, then check if user has already reported this article
     * or not.
     *
     * @return true if the user has not reported, otherwise false.
     */
    private fun canReport(): Boolean {
        val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        if (prefs.getString("email", null).equals(null))
            return false

        val user = UserTableHelper(this)
            .getUserByEmail(prefs.getString("email", null))
        val articleId = intent.getIntExtra("article_id", -1)
        val article = ReportedArticleTableHelper(this)
            .checkUserReport(user!!.id, articleId)

        return article == null
    }
}
