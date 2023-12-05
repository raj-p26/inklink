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

import java.util.ArrayList

internal class AdminListUserAdapter(private val context: Context) :
    RecyclerView.Adapter<AdminListUserAdapter.ViewHolder>() {
    private val users: ArrayList<User>

    init {
        val helper = UserTableHelper(context)
        users = helper.users
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.admin_list_user_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.userEmail.text = user.email
        holder.user.id = user.id
        holder.user.email = user.email
    }

    override fun getItemCount(): Int {
        return users.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val userEmail: TextView = itemView.findViewById(R.id.alu_user_email)
        val user: User = User()

        init {
            val deleteButton = itemView.findViewById<Button>(R.id.alu_delete_button)

            deleteButton.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            showDialog()
        }

        private fun showDialog() {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Suspend " + user.email + "?")
            builder.setMessage("Are you sure you want to suspend this user's account?")
            builder.setPositiveButton("Yes") { _, _ ->
                val helper = UserTableHelper(context)
                helper.suspendUser(user.id)

                users.removeAt(bindingAdapterPosition)
                notifyItemRemoved(bindingAdapterPosition)
            }
            builder.setNegativeButton("No", null)

            builder.create().show()
        }
    }
}
