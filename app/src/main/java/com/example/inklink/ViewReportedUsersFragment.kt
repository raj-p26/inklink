package com.example.inklink


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inklink.adapters.ReportedUsersAdapter

class ViewReportedUsersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_reported_users, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.vru_recycler_view)
        recyclerView.adapter = ReportedUsersAdapter(activity!!.applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }


}
