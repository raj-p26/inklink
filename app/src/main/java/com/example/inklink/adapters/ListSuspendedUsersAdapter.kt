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
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.User

class ListSuspendedUsersAdapter(private val context: Context) :
    RecyclerView.Adapter<ListSuspendedUsersAdapter.ViewHolder>() {
    private val suspendedUsers: ArrayList<User>

    init {
        val helper = UserTableHelper(context)
        suspendedUsers = helper.getSuspendedUsers()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.suspended_user_row, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return suspendedUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = suspendedUsers[position]
        holder.tvUsername.text = user.username
        holder.user.id = user.id
        holder.user.username = user.username
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user = User()
        val tvUsername: TextView = itemView.findViewById(R.id.sur_username)

        init {
            val revert: Button = itemView.findViewById(R.id.revert_suspend)

            revert.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Revert Suspension?")
                builder.setMessage("Are you sure you want to revert suspension of ${user.username}?")
                builder.setPositiveButton("Yes") {_, _ ->
                    val helper = UserTableHelper(context)
                    helper.revertSuspension(user.id)

                    suspendedUsers.removeAt(bindingAdapterPosition)
                    notifyItemRemoved(bindingAdapterPosition)
                }
                builder.setNegativeButton("No", null)
                builder.create().show()
            }
        }
    }
}