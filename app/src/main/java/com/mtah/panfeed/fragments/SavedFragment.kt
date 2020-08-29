package com.mtah.panfeed.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mtah.panfeed.MainActivity

import com.mtah.panfeed.R
import com.mtah.panfeed.ReadActivity
import com.mtah.panfeed.SavedViewModel
import com.mtah.panfeed.adapters.SavedNewsAdapter
import com.mtah.panfeed.models.Article


 class SavedFragment : Fragment(), SavedNewsAdapter.OnSavedNewsClickListener {
    private lateinit var savedViewModel: SavedViewModel
    private lateinit var savedNewsList: List<Article>
    private val TAG = "SavedFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)

        val savedNewsRecyclerView: RecyclerView = view.findViewById(R.id.savedNewsRecyclerView)
        savedNewsRecyclerView.layoutManager = LinearLayoutManager(context)
        val savedNewsAdapter = SavedNewsAdapter(this)
        savedNewsRecyclerView.adapter = savedNewsAdapter

        savedViewModel = ViewModelProvider(requireActivity()).get(SavedViewModel::class.java)
        savedViewModel.getAll().observe(viewLifecycleOwner, { news ->
            savedNewsAdapter.setNewsList(news)
            savedNewsList = news
        })

        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.savedSwipeRefresh)
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            savedNewsList.forEach{ Log.d(TAG, "checkDB: ${it.url}")}
            swipeRefresh.isRefreshing = false
        }

        return view
    }

     override fun onItemClick(article: Article) {
         val readIntent = Intent(context, ReadActivity::class.java)
         readIntent.putExtra(MainActivity.EXTRA_TITLE, article.title)
         readIntent.putExtra(MainActivity.EXTRA_URL, article.url)
         readIntent.putExtra(MainActivity.EXTRA_IMAGE_URL, article.urlToImage)
         readIntent.putExtra(MainActivity.EXTRA_DATE, article.publishedAt)

         Log.i(TAG, "onItemClick: Opening ${article.url}")
         startActivity(readIntent)
     }


 }
