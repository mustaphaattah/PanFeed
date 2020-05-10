package com.mtah.panfeed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mtah.panfeed.models.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [GlobalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GlobalFragment : Fragment() {
    val TAG = "GlobalFragment"
    val COVID_KEYWORD = "coronavirus"
    val SORT_BY = "publishedAt"
    private val PAGE_SIZE = 100
    var recyclerView: RecyclerView? = null
    var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_global, container, false)

        recyclerView = view.findViewById(R.id.globalRecyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)

        swipeRefresh = view.findViewById(R.id.globalRefresh)
        swipeRefresh!!.setOnRefreshListener { fetchNews() }

        //fill the recycler view with news cards
        fetchNews()

        return view
    }

    private fun fetchNews(){
        swipeRefresh!!.isRefreshing = true
        Log.d(TAG, "fetching News")
        val request = NewsApiClient.getApi(NewsInterface::class.java)
        val lang = getLanguage()
        val call = request.getAllCovidNews(getString(R.string.api_key), COVID_KEYWORD, lang, PAGE_SIZE, SORT_BY)
        Log.d(TAG, "news language $lang")

        Log.d(TAG, "Sending requests...")
        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                swipeRefresh!!.isRefreshing = false
                Log.e(TAG, "Response Failure")
                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT)
                Log.e(TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                swipeRefresh!!.isRefreshing = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse successful: Showing articles")

                    recyclerView!!.apply {
                        //layoutManager = LinearLayoutManager(this@HomeActivity)
                        itemAnimator = DefaultItemAnimator()
                        adapter = NewsAdapter(response.body()!!.articles, context)

                        val newsCount = (adapter as NewsAdapter).itemCount
                        Log.d(TAG, "onResponse successful:  Done! got $newsCount articles")
                        if (newsCount == 0)
                            Toast.makeText(activity, "No Global news to show", Toast.LENGTH_LONG)
                    }

                }
            }
        })

    }

    private fun getLanguage() : String{
        return Locale.getDefault().language
    }
}
