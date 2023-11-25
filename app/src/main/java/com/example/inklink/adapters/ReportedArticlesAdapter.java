package com.example.inklink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inklink.R;
import com.example.inklink.dbhelpers.ArticleTableHelper;
import com.example.inklink.dbhelpers.ReportedArticleTableHelper;
import com.example.inklink.models.Article;
import com.example.inklink.models.ReportedArticles;

import java.util.ArrayList;

public class ReportedArticlesAdapter extends
        RecyclerView.Adapter<ReportedArticlesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReportedArticles> reportedArticles;

    public ReportedArticlesAdapter(Context context) {
        this.context = context;
        this.reportedArticles= new ReportedArticleTableHelper(context)
            .getAllReportedArticles();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.reported_articles_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportedArticles article = reportedArticles.get(position);
        Article a = new ArticleTableHelper(context)
                .getArticle(article.getArticleId());

        holder.article.setReportStatus("handled");
        holder.tvTitle.setText(a.getArticleTitle());
        holder.article.setArticleId(article.getArticleId());
    }

    @Override
    public int getItemCount() {
        return reportedArticles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        private TextView tvTitle;
        private ReportedArticles article;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            article = new ReportedArticles();
            tvTitle = itemView.findViewById(R.id.rar_article_title);

            Button button = itemView.findViewById(R.id.rar_delete_btn);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ReportedArticleTableHelper helper = new ReportedArticleTableHelper(context);
            helper.updateReportStatus(article);

            reportedArticles.remove(getBindingAdapterPosition());
            notifyItemRemoved(getBindingAdapterPosition());
        }
    }
}
