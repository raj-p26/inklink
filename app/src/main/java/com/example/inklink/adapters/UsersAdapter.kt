package com.example.inklink.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.example.inklink.R
import com.example.inklink.ViewUserActivity
import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.User

import java.util.ArrayList

internal class UsersAdapter(private val context: Context) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private val users: ArrayList<User>

    init {
        val helper = UserTableHelper(context)
        users = helper.users
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val myView = inflater.inflate(R.layout.list_users_row, parent, false)

        return ViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = users[position].username
        holder.userEmail.text = users[position].email
    }

    override fun getItemCount(): Int {
        return users.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val username: TextView
        val userEmail: TextView

        init {
            val cardView = itemView.findViewById<CardView>(R.id.lu_row_container)
            cardView.setOnClickListener(this)
            username = itemView.findViewById(R.id.lu_username)
            userEmail = itemView.findViewById(R.id.lu_email)
        }

        override fun onClick(v: View) {
            val intent = Intent(context, ViewUserActivity::class.java)
            val user = UserTableHelper(context)
                .getUserByEmail(userEmail.text.toString())

            intent.putExtra("user_id", user!!.id)
            intent.putExtra("user_first_name", user.firstName)
            intent.putExtra("user_last_name", user.lastName)
            intent.putExtra("user_username", user.username)
            intent.putExtra("user_email", user.email)
            intent.putExtra("user_about", user.about)
            intent.putExtra("account_status", user.accountStatus)

            context.startActivity(intent)
        }
    }
}
