package com.example.inklink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inklink.adapters.ArticlesAdapter

class ArticlesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_articles, container, false)
        recyclerView = view.findViewById(R.id.articles_recycler_view)

        recyclerView.adapter = ArticlesAdapter(context, "all")
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }
}
