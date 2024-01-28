package com.example.inklink

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inklink.adapters.ListBannedArticlesAdapter

class ListBannedArticleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_banned_article, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.alba_recycler_view)
        recyclerView.adapter = ListBannedArticlesAdapter(context!!)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }
}