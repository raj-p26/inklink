package com.example.inklink.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inklink.R;
import com.example.inklink.dbhelpers.UserTableHelper;
import com.example.inklink.models.User;

import java.util.ArrayList;

public class AdminListUserAdapter extends RecyclerView.Adapter<AdminListUserAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> users;

    public AdminListUserAdapter(Context context) {
        this.context = context;
        UserTableHelper helper = new UserTableHelper(context);
        users = helper.getUsers();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.admin_list_user_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.userEmail.setText(user.getEmail());
        holder.user.setId(user.getId());
        holder.user.setEmail(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView userEmail;
        private User user;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userEmail = itemView.findViewById(R.id.alu_user_email);
            this.user = new User();
            Button deleteButton = itemView.findViewById(R.id.alu_delete_button);

            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showDialog();
        }

        private void showDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Suspend " + user.getEmail() + "?");
            builder.setMessage("Are you sure you want to suspend this user's account?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UserTableHelper helper = new UserTableHelper(context);
                    helper.suspendUser(user.getId());

                    users.remove(getBindingAdapterPosition());
                    notifyItemRemoved(getBindingAdapterPosition());
                }
            });
            builder.setNegativeButton("No", null);

            builder.create().show();
        }
    }
}
