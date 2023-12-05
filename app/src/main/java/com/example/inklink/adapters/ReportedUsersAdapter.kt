package com.example.inklink.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.inklink.R
import com.example.inklink.dbhelpers.ReportedUserTableHelper
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.ReportedUsers

import java.util.ArrayList

internal class ReportedUsersAdapter(private val context: Context) :
    RecyclerView.Adapter<ReportedUsersAdapter.ViewHolder>() {
    private val list: ArrayList<ReportedUsers> = ReportedUserTableHelper(context)
        .allReports

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.report_user_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reportedUser = list[position]
        val user = UserTableHelper(context)
            .getUserById(reportedUser.userId)

        holder.view.text = user!!.username
        holder.user.userId = reportedUser.userId
        holder.user.reportStatus = "handled"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val view: TextView
        val user: ReportedUsers

        init {
            val button = itemView.findViewById<Button>(R.id.demo_delete_btn)
            user = ReportedUsers()
            this.view = itemView.findViewById(R.id.demoTv)
            button.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            delete()
        }

        private fun delete() {
            val helper = ReportedUserTableHelper(context)
            helper.updateReportStatus(user)

            list.removeAt(bindingAdapterPosition)
            notifyItemRemoved(bindingAdapterPosition)
        }
    }
}
