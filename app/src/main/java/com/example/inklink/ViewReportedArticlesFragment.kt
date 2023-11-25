package com.example.inklink


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inklink.adapters.ReportedArticlesAdapter

class ViewReportedArticlesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_reported_articles, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.vra_recycler_view)
        recyclerView.adapter = ReportedArticlesAdapter(context)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }


}
