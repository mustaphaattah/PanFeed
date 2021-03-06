package com.mtah.panfeed.fragments.save

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
import com.google.android.material.snackbar.Snackbar
import com.mtah.panfeed.MainActivity

import com.mtah.panfeed.R
import com.mtah.panfeed.ReadActivity
import com.mtah.panfeed.adapters.SavedNewsAdapter
import com.mtah.panfeed.models.Article
import kotlinx.android.synthetic.main.fragment_saved.view.*


class SavedFragment : Fragment(), SavedNewsAdapter.OnSavedNewsClickListener {
     private lateinit var savedViewModel: SavedViewModel
     private var savedNewsList: MutableList<Article> = mutableListOf()
     private lateinit var emptySaveTextView: TextView
     private lateinit var savedNewsAdapter: SavedNewsAdapter
     private lateinit var deletedArticle: Article
    private val TAG = "SavedFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)
        setHasOptionsMenu(true)

        val savedNewsRecyclerView: RecyclerView = view.findViewById(R.id.savedNewsRecyclerView)
        savedNewsRecyclerView.layoutManager = LinearLayoutManager(context)
        savedNewsAdapter = SavedNewsAdapter(this)
        savedNewsRecyclerView.adapter = savedNewsAdapter



        savedViewModel = ViewModelProvider(requireActivity()).get(SavedViewModel::class.java)
        savedViewModel.getAll().observe(viewLifecycleOwner, { news ->
            if (news.isNotEmpty()) {
                view.noDataTextView.visibility = View.INVISIBLE
                view.noDataImageView.visibility = View.INVISIBLE
            } else {
                view.noDataTextView.visibility = View.VISIBLE
                view.noDataImageView.visibility = View.VISIBLE
            }

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
                removeArticle(viewHolder)
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

     private fun removeArticle(viewHolder: RecyclerView.ViewHolder){
         val position = viewHolder.adapterPosition
         deletedArticle = savedNewsAdapter.getItemAt(position )
         savedViewModel.delete(deletedArticle)
         savedNewsAdapter.notifyItemRemoved(position)
         Log.i(TAG, "removeArticle: Article removed")
         Snackbar.make(viewHolder.itemView, "Article removed.", Snackbar.LENGTH_LONG)
             .setAction("UNDO") {
                 savedNewsAdapter.add(position, deletedArticle)
                 savedViewModel.insert(deletedArticle)
                 Log.i(TAG, "removeArticle: Delete Undone")
         }.show()
     }

 }
