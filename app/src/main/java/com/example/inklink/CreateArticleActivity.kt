package com.example.inklink

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.inklink.dbhelpers.ArticleTableHelper
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.Article
import com.example.inklink.models.User

/*
*
* every view starts with `ca` prefix to ensure that this is `CreateArticle` activity
*
* */
class CreateArticleActivity : AppCompatActivity() {
    private lateinit var caTitle: EditText
    private lateinit var caContent: EditText
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_article)
        supportActionBar?.title = "Create a new Article"

        val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val intent = intent

        user = UserTableHelper(this)
            .getUserByEmail(prefs.getString("email", null))!!

        caTitle = findViewById(R.id.ca_title)
        caContent = findViewById(R.id.ca_content)

        if (intent.getStringExtra("article_content") != null) {
            caTitle.setText(intent.getStringExtra("article_title"))
            caContent.setText(intent.getStringExtra("article_content"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val intent = intent
        if (intent.getBooleanExtra("is_draft", false))
            menu?.add(Menu.NONE, R.integer.ca_make_live_opt_id, Menu.NONE, "Make Live(Publish)")
        else
            menu?.add(Menu.NONE, R.integer.ca_publish_opt_id, Menu.NONE, "Publish Article")
        menu?.add(Menu.NONE, R.integer.ca_save_as_draft_opt_id, Menu.NONE, "Save as Draft")
        menu?.add(Menu.NONE, R.integer.ca_discard_opt_id, Menu.NONE, "Discard")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.integer.ca_publish_opt_id -> {
                if (caTitle.text.isEmpty() || caContent.text.isEmpty())
                    Toast.makeText(
                        this,
                        "Please enter some content",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    publishArticleAs("published")
                    Toast.makeText(
                        this,
                        "Article Published!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
            R.integer.ca_save_as_draft_opt_id -> {
                if (caTitle.text.isEmpty() || caContent.text.isEmpty())
                    Toast.makeText(this, "Please enter some content", Toast.LENGTH_SHORT).show()
                else{
                    publishArticleAs("draft")
                    Toast.makeText(this, "Article Saved As Draft!", Toast.LENGTH_SHORT).show()
                }
            }
            R.integer.ca_discard_opt_id -> {
                if (caTitle.text.isNotEmpty() || caContent.text.isNotEmpty()) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Discard?")
                    builder.setMessage("Are you sure you want to discard this article?")
                    builder.setPositiveButton("Yes") {_, _ ->
                        finish()
                    }
                    builder.setNegativeButton("No", null)

                    builder.create().show()
                } else finish()
            }

            R.integer.ca_make_live_opt_id -> {
                makeArticleLive()
                Toast.makeText(this, "Your article is Live Now!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * This method is used to save article as given status.
     *
     * @param status the status of the article.
     */
    private fun publishArticleAs(status: String) {
        val helper = ArticleTableHelper(this)
        val article = Article()

        article.articleTitle = caTitle.text.toString()
        article.articleContent = caContent.text.toString()
        article.userId = user.id
        article.articleStatus = status

        helper.addArticle(article)

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
        finish()
    }

    /**
     * This method is used to update the status of article to 'published'.
     */
    private fun makeArticleLive() {
        val helper = ArticleTableHelper(this)
        val article = Article()
        val intent = intent

        article.articleTitle = caTitle.text.toString()
        article.articleContent = caContent.text.toString()
        article.id = intent.getIntExtra("article_id", -1)
        article.articleStatus = "published"

        helper.updateArticleStatus(article)
    }
}
