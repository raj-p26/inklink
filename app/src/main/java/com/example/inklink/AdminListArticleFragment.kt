package com.example.inklink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inklink.adapters.AdminListArticlesAdapter

class AdminListArticleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_admin_list_article,
            container,
            false
        )

        activity?.actionBar?.title = "All Posted Articles"

        val recyclerView: RecyclerView = view.findViewById(R.id.ala_recycler_view)

        recyclerView.adapter = AdminListArticlesAdapter(activity!!.applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }
}
