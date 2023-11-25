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
import com.example.inklink.dbhelpers.ReportedUserTableHelper;
import com.example.inklink.dbhelpers.UserTableHelper;
import com.example.inklink.models.ReportedUsers;
import com.example.inklink.models.User;

import java.util.ArrayList;

public class ReportedUsersAdapter extends RecyclerView.Adapter<ReportedUsersAdapter.ViewHolder> {
    private ArrayList<ReportedUsers> list;
    private Context context;
    public ReportedUsersAdapter(Context context) {
        this.context = context;
        list = new ReportedUserTableHelper(context)
                .getAllReports();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.report_user_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportedUsers reportedUser = list.get(position);
        User user = new UserTableHelper(context)
                .getUserById(reportedUser.getUserId());

        holder.view.setText(user.getUsername());
        holder.user.setUserId(reportedUser.getUserId());
        holder.user.setReportStatus("handled");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView view;
        private ReportedUsers user;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Button button = itemView.findViewById(R.id.demo_delete_btn);
            user = new ReportedUsers();
            this.view = itemView.findViewById(R.id.demoTv);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            delete();
        }

        void delete() {
            ReportedUserTableHelper helper = new ReportedUserTableHelper(context);
            helper.updateReportStatus(user);

            list.remove(getBindingAdapterPosition());
            notifyItemRemoved(getBindingAdapterPosition());
        }
    }
}
