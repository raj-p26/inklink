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
import com.example.inklink.ViewUserActivity;
import com.example.inklink.dbhelpers.UserTableHelper;
import com.example.inklink.models.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private ArrayList<User> users;
    private Context context;

    public UsersAdapter(Context context) {
        this.context = context;
        UserTableHelper helper = new UserTableHelper(context);
        users = helper.getUsers();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View myView = inflater.inflate(R.layout.list_users_row, parent, false);

        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(users.get(position).getUsername());
        holder.userEmail.setText(users.get(position).getEmail());
    }

    @Override
    public int getItemCount() { return users.size(); }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView username, userEmail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView cardView = itemView.findViewById(R.id.lu_row_container);
            cardView.setOnClickListener(this);
            username = itemView.findViewById(R.id.lu_username);
            userEmail = itemView.findViewById(R.id.lu_email);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ViewUserActivity.class);
            User user = new UserTableHelper(context)
                    .getUserByEmail(userEmail.getText().toString());

            intent.putExtra("user_id", user.getId());
            intent.putExtra("user_first_name", user.getFirstName());
            intent.putExtra("user_last_name", user.getLastName());
            intent.putExtra("user_username", user.getUsername());
            intent.putExtra("user_email", user.getEmail());
            intent.putExtra("user_about", user.getAbout());
            intent.putExtra("account_status", user.getAccountStatus());

            context.startActivity(intent);
        }
    }
}
