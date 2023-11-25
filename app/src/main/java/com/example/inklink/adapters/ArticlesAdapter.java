package com.example.inklink.adapters;

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
import com.example.inklink.dbhelpers.UserTableHelper;
import com.example.inklink.models.Article;
import com.example.inklink.models.User;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Article> list;

    public ArticlesAdapter(Context context, String type) {
        this.context = context;
        ArticleTableHelper helper = new ArticleTableHelper(context);
        if (type.equals("latest"))
            this.list = helper.getLatestArticles();
        else
            this.list = helper.getAllArticles();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.new_articles_row, parent, false);

        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getArticleTitle());
        String authorName = getAuthor(list.get(position).getUserId());
        holder.tvAuthor.setText(authorName);
        holder.content = list.get(position).getArticleContent();
        holder.id = list.get(position).getUserId();
        holder.articleId = list.get(position).getId();
        holder.status = list.get(position).getArticleStatus();
    }

    @Override
    public int getItemCount() { return list.size(); }

    private String getAuthor(int id) {
        User user = new UserTableHelper(context)
                .getUserById(id);
        return user.getUsername();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvAuthor;
        private String content, status;
        private int id, articleId;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.new_article_title);
            this.tvAuthor = itemView.findViewById(R.id.new_article_author);
            CardView cardView = itemView.findViewById(R.id.new_article_row_container);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ViewArticleActivity.class);
            intent.putExtra("article_title", tvTitle.getText().toString());
            intent.putExtra("article_author", tvAuthor.getText().toString());
            intent.putExtra("author_id", id);
            intent.putExtra("article_id", articleId);
            intent.putExtra("article_status", status);
            intent.putExtra("article_content", content);

            context.startActivity(intent);
        }
    }
}
