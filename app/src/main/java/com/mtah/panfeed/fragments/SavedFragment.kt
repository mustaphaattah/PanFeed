package com.mtah.panfeed.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
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
     private var savedNewsList: MutableList<Article> = mutableListOf()
     private lateinit var emptySaveTextView: TextView
    private val TAG = "SavedFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)
        setHasOptionsMenu(true)

        val savedNewsRecyclerView: RecyclerView = view.findViewById(R.id.savedNewsRecyclerView)
        savedNewsRecyclerView.layoutManager = LinearLayoutManager(context)
        val savedNewsAdapter = SavedNewsAdapter(this)
        savedNewsRecyclerView.adapter = savedNewsAdapter

        emptySaveTextView = view.findViewById(R.id.saveEmptyText)


        savedViewModel = ViewModelProvider(requireActivity()).get(SavedViewModel::class.java)
        savedViewModel.getAll().observe(viewLifecycleOwner, { news ->
            emptySaveTextView.visibility = if (news.isNotEmpty()) View.GONE else View.VISIBLE
            savedNewsList = news as MutableList<Article>
            savedNewsAdapter.setNewsList(savedNewsList)
        })


        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                savedViewModel.delete(savedNewsAdapter.getItemAt(viewHolder.adapterPosition))
                Toast.makeText(context, "Article deleted.", Toast.LENGTH_SHORT).show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(savedNewsRecyclerView)




        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.savedSwipeRefresh)
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            savedNewsList.forEach{ Log.d(TAG, "checkDB: ${it.url}")}
            swipeRefresh.isRefreshing = false
        }

        return view
    }


     override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         super.onCreateOptionsMenu(menu, inflater)
         inflater.inflate(R.menu.saved_menu, menu)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
             R.id.delete_all_option -> {
                 savedViewModel.deleteAll()
                 Toast.makeText(context, "All articles deleted.", Toast.LENGTH_SHORT).show()
             }
         }
         return super.onOptionsItemSelected(item)
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
