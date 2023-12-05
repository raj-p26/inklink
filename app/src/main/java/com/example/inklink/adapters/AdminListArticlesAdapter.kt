package com.example.inklink.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.example.inklink.R
import com.example.inklink.ViewArticleActivity
import com.example.inklink.dbhelpers.ArticleTableHelper
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.Article

import java.util.ArrayList

internal class AdminListArticlesAdapter(private val context: Context) :
    RecyclerView.Adapter<AdminListArticlesAdapter.ViewHolder>() {
    private val articles: ArrayList<Article>

    init {
        val helper = ArticleTableHelper(context)
        articles = helper.publishedArticles
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.admin_list_articles_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        val author = UserTableHelper(context)
            .getUserById(article.userId)

        holder.article.id = article.id
        holder.article.articleStatus = "banned"
        holder.article.articleContent = article.articleContent
        holder.article.articleTitle = article.articleTitle
        holder.article.userId = article.userId

        holder.tvTitle.text = article.articleTitle
        holder.tvAuthor.text = author!!.username
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val tvTitle: TextView = itemView.findViewById(R.id.ala_article_title)
        val tvAuthor: TextView = itemView.findViewById(R.id.ala_article_author)
        val article: Article = Article()

        init {
            val button = itemView.findViewById<Button>(R.id.ala_delete_button)
            button.setOnClickListener(this)
            val cardView = itemView.findViewById<CardView>(R.id.ala_row_container)
            cardView.setOnClickListener {
                val intent = Intent(context, ViewArticleActivity::class.java)
                intent.putExtra("article_content", article.articleContent)
                intent.putExtra("article_title", article.articleTitle)
                intent.putExtra("author_id", article.userId)

                context.startActivity(intent)
            }
        }

        override fun onClick(v: View) {
            delete()
        }

        private fun delete() {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Ban Article?")
            builder.setMessage("Are you sure you want to ban this article?")
            builder.setPositiveButton("Yes") { _, _ ->
                val helper = ArticleTableHelper(context)
                helper.updateArticleStatus(article)

                articles.removeAt(bindingAdapterPosition)
                notifyItemRemoved(bindingAdapterPosition)
            }
            builder.setNegativeButton("No", null)

            builder.create().show()
        }
    }
}
