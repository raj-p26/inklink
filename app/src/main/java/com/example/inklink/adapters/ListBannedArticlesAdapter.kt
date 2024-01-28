package com.example.inklink.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.example.inklink.R
import com.example.inklink.dbhelpers.ArticleTableHelper
import com.example.inklink.models.Article

class ListBannedArticlesAdapter(private val context: Context) :
    RecyclerView.Adapter<ListBannedArticlesAdapter.ViewHolder>() {
    private val bannedArticles: ArrayList<Article>
    init {
        val helper = ArticleTableHelper(context)
        bannedArticles = helper.getBannedArticles()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.banned_articles_row, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return bannedArticles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = bannedArticles[position]
        holder.article = article
        holder.tvTitle.text = article.articleTitle
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var article = Article()
        val tvTitle: TextView = itemView.findViewById(R.id.bar_article_title)

        init {
            val unban: Button = itemView.findViewById(R.id.unban_btn)
            unban.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Unban article?")
                builder.setMessage("Are you sure you want to unban article '${article.articleTitle}'?")
                builder.setPositiveButton("Yes") {_, _ ->
                    val helper = ArticleTableHelper(context)
                    helper.unbanArticle(article.id)

                    bannedArticles.removeAt(bindingAdapterPosition)
                    notifyItemRemoved(bindingAdapterPosition)
                }
                builder.setNegativeButton("No", null)
                builder.create().show()
            }
        }
    }
}