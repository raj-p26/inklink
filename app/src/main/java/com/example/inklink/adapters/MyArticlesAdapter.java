package com.example.inklink.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inklink.R;
import com.example.inklink.ViewArticleActivity;
import com.example.inklink.dbhelpers.ArticleTableHelper;
import com.example.inklink.models.Article;

import java.util.ArrayList;

public class MyArticlesAdapter extends RecyclerView.Adapter<MyArticlesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Article> articles;
    private Activity activity;

    public MyArticlesAdapter(Activity activity, Context context, int id) {
        this.activity = activity;
        this.context = context;
        ArticleTableHelper helper = new ArticleTableHelper(context);
        this.articles = helper.getUserSpecificArticles(id);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View myView = inflater.inflate(R.layout.my_posts_row, parent, false);

        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.maTitle.setText(articles.get(position).getArticleTitle());
        holder.maStatus.setText(articles.get(position).getArticleStatus());
        holder.content = articles.get(position).getArticleContent();
        holder.authorId = articles.get(position).getUserId();
        holder.articleId = articles.get(position).getId();
    }

    @Override
    public int getItemCount() { return articles.size(); }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView maTitle, maStatus;
        private String content;
        private int authorId, articleId;

        ViewHolder(View itemView) {
            super(itemView);

            maTitle = itemView.findViewById(R.id.mp_post_title);
            maStatus = itemView.findViewById(R.id.mp_post_status);

            CardView cardView = itemView.findViewById(R.id.my_posts_row_container);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ViewArticleActivity.class);
            intent.putExtra("article_title", maTitle.getText().toString());
            intent.putExtra("article_status", maStatus.getText().toString());
            intent.putExtra("author_id", authorId);
            intent.putExtra("article_content", content);
            intent.putExtra("article_id", articleId);

            activity.startActivityForResult(intent, 1);
        }
    }
}
