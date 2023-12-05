package com.example.inklink.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.example.inklink.R
import com.example.inklink.ViewArticleActivity
import com.example.inklink.dbhelpers.ArticleTableHelper
import com.example.inklink.models.Article

import java.util.ArrayList

internal class MyArticlesAdapter(private val activity: Activity, private val context: Context, id: Int?) :
    RecyclerView.Adapter<MyArticlesAdapter.ViewHolder>() {
    private val articles: ArrayList<Article>

    init {
        val helper = ArticleTableHelper(context)
        this.articles = helper.getUserSpecificArticles(id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val myView = inflater.inflate(R.layout.my_posts_row, parent, false)

        return ViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.maTitle.text = articles[position].articleTitle
        holder.maStatus.text = articles[position].articleStatus
        holder.content = articles[position].articleContent
        holder.authorId = articles[position].userId
        holder.articleId = articles[position].id
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val maTitle: TextView = itemView.findViewById(R.id.mp_post_title)
        val maStatus: TextView = itemView.findViewById(R.id.mp_post_status)
        var content: String? = null
        var authorId: Int = 0
        var articleId: Int = 0

        init {

            val cardView = itemView.findViewById<CardView>(R.id.my_posts_row_container)
            cardView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val intent = Intent(context, ViewArticleActivity::class.java)
            intent.putExtra("article_title", maTitle.text.toString())
            intent.putExtra("article_status", maStatus.text.toString())
            intent.putExtra("author_id", authorId)
            intent.putExtra("article_content", content)
            intent.putExtra("article_id", articleId)

            activity.startActivityForResult(intent, 1)
        }
    }
}
