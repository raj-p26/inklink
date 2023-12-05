package com.example.inklink

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.inklink.adapters.ArticlesAdapter

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView =  inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = myView.findViewById(R.id.new_articles_recycle_view)

        recyclerView.adapter = ArticlesAdapter(activity!!.applicationContext, "latest")
        recyclerView.layoutManager = LinearLayoutManager(context)

        return myView
    }
}
