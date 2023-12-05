package com.example.inklink

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inklink.adapters.ArticlesAdapter
import com.example.inklink.adapters.MyArticlesAdapter
import com.example.inklink.dbhelpers.UserTableHelper

class MyPostsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView =  inflater.inflate(R.layout.fragment_my_posts, container, false)
        val userDetails = activity?.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = UserTableHelper(activity!!.applicationContext)
            .getUserByEmail(userDetails?.getString("email", null))
            ?.id

        recyclerView = myView.findViewById(R.id.my_posts_recycler_view)

        recyclerView.adapter = MyArticlesAdapter(activity!!, activity!!.applicationContext, userId)


        recyclerView.layoutManager = LinearLayoutManager(context)

        return myView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        activity?.recreate()
    }
}
