package com.example.inklink.adapters

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
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.Article

import java.util.ArrayList

internal class ArticlesAdapter(private val context: Context, type: String) :
    RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {
    private var list: ArrayList<Article>? = null

    init {
        val helper = ArticleTableHelper(context)
        if (type == "latest")
            this.list = helper.latestArticles
        else
            this.list = helper.allArticles
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val myView = inflater.inflate(R.layout.new_articles_row, parent, false)

        return ViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = list!![position].articleTitle
        val authorName = getAuthor(list!![position].userId)
        holder.tvAuthor.text = authorName
        holder.content = list!![position].articleContent
        holder.id = list!![position].userId
        holder.articleId = list!![position].id
        holder.status = list!![position].articleStatus
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    private fun getAuthor(id: Int): String? {
        val user = UserTableHelper(context)
            .getUserById(id)
        return user!!.username
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val tvTitle: TextView = itemView.findViewById(R.id.new_article_title)
        val tvAuthor: TextView = itemView.findViewById(R.id.new_article_author)
        var content: String? = null
        var status: String? = null
        var id: Int = 0
        var articleId: Int = 0

        init {
            val cardView = itemView.findViewById<CardView>(R.id.new_article_row_container)
            cardView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val intent = Intent(context, ViewArticleActivity::class.java)
            intent.putExtra("article_title", tvTitle.text.toString())
            intent.putExtra("article_author", tvAuthor.text.toString())
            intent.putExtra("author_id", id)
            intent.putExtra("article_id", articleId)
            intent.putExtra("article_status", status)
            intent.putExtra("article_content", content)

            context.startActivity(intent)
        }
    }
}
