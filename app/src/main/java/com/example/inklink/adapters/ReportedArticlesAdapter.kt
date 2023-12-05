package com.example.inklink.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.inklink.R
import com.example.inklink.dbhelpers.ArticleTableHelper
import com.example.inklink.dbhelpers.ReportedArticleTableHelper
import com.example.inklink.models.Article
import com.example.inklink.models.ReportedArticles

import java.util.ArrayList

internal class ReportedArticlesAdapter(private val context: Context) :
    RecyclerView.Adapter<ReportedArticlesAdapter.ViewHolder>() {
    private val reportedArticles: ArrayList<ReportedArticles>

    init {
        this.reportedArticles = ReportedArticleTableHelper(context)
            .allReportedArticles
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.reported_articles_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = reportedArticles[position]
        val a = ArticleTableHelper(context)
            .getArticle(article.articleId)

        holder.article.reportStatus = "handled"
        holder.tvTitle.text = a.articleTitle
        holder.article.articleId = article.articleId
    }

    override fun getItemCount(): Int {
        return reportedArticles.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val tvTitle: TextView
        val article: ReportedArticles

        init {
            article = ReportedArticles()
            tvTitle = itemView.findViewById(R.id.rar_article_title)

            val button = itemView.findViewById<Button>(R.id.rar_delete_btn)
            button.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val helper = ReportedArticleTableHelper(context)
            helper.updateReportStatus(article)

            reportedArticles.removeAt(bindingAdapterPosition)
            notifyItemRemoved(bindingAdapterPosition)
        }
    }
}
