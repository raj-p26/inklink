package com.example.inklink.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AdminListArticlesAdapter extends
        RecyclerView.Adapter<AdminListArticlesAdapter.ViewHolder> {
    private ArrayList<Article> articles;
    private Context context;

    public AdminListArticlesAdapter(Context context) {
        this.context = context;
        ArticleTableHelper helper = new ArticleTableHelper(context);
        articles = helper.getPublishedArticles();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.admin_list_articles_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);
        User author = new UserTableHelper(context)
                .getUserById(article.getUserId());

        holder.article.setId(article.getId());
        holder.article.setArticleStatus("banned");
        holder.article.setArticleContent(article.getArticleContent());
        holder.article.setArticleTitle(article.getArticleTitle());
        holder.article.setUserId(article.getUserId());

        holder.tvTitle.setText(article.getArticleTitle());
        holder.tvAuthor.setText(author.getUsername());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView tvTitle, tvAuthor;
        private Article article;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.ala_article_title);
            this.tvAuthor = itemView.findViewById(R.id.ala_article_author);
            this.article = new Article();
            Button button = itemView.findViewById(R.id.ala_delete_button);
            button.setOnClickListener(this);
            CardView cardView = itemView.findViewById(R.id.ala_row_container);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewArticleActivity.class);
                    intent.putExtra("article_content", article.getArticleContent());
                    intent.putExtra("article_title", article.getArticleTitle());
                    intent.putExtra("author_id", article.getUserId());

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            delete();
        }

        void delete() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Ban Article?");
            builder.setMessage("Are you sure you want to ban this article?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ArticleTableHelper helper = new ArticleTableHelper(context);
                    helper.updateArticleStatus(article);

                    articles.remove(getBindingAdapterPosition());
                    notifyItemRemoved(getBindingAdapterPosition());
                }
            });
            builder.setNegativeButton("No", null);

            builder.create().show();
        }
    }
}
