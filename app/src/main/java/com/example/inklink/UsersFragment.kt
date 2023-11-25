package com.example.inklink

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inklink.adapters.UsersAdapter

class UsersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_users, container, false)
        val recyclerView: RecyclerView = myView.findViewById(R.id.list_users_recycler_view)

        recyclerView.adapter = UsersAdapter(activity)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return myView
    }
}
