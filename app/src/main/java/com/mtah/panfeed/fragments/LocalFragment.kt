package com.mtah.panfeed.fragments

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
import com.mtah.panfeed.adapters.NewsAdapter
import com.mtah.panfeed.api.NewsApiClient
import com.mtah.panfeed.api.NewsInterface
import com.mtah.panfeed.R
import com.mtah.panfeed.models.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [LocalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocalFragment : Fragment() {

    val TAG = "LocalFragment"
    val COVID_KEYWORD = "coronavirus"
    val PAGE_SIZE = 50

    var recyclerView: RecyclerView? = null
    var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_local, container, false)

        recyclerView = view.findViewById(R.id.localRecyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)

        swipeRefresh = view.findViewById(R.id.localSwipeRefresh)
        swipeRefresh!!.setOnRefreshListener { fetchLocalNews() }

        fetchLocalNews()


        return view
    }

    fun fetchLocalNews(){
        swipeRefresh!!.isRefreshing = true
        val request =
            NewsApiClient.getApi(NewsInterface::class.java)
        val country = getCountryCode()
        val call = request.getLocalNews(getString(R.string.api_key), country, COVID_KEYWORD, PAGE_SIZE)
        Log.d(TAG, "the locale code: $country")

        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                swipeRefresh!!.isRefreshing = false
                Toast.makeText(activity, "Unable to get Local news", Toast.LENGTH_SHORT)
                Log.e(TAG, "Unable to get Local news")
                Log.e(TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                swipeRefresh!!.isRefreshing = false
                if (response.isSuccessful){
                    recyclerView!!.apply {
                        itemAnimator = DefaultItemAnimator()
                        adapter = NewsAdapter(
                            response.body()!!.articles,
                            context
                        )

                        val newsCount = (adapter as NewsAdapter).itemCount
                        Log.d(TAG, "onResponse successful:  Done! got $newsCount articles")
                        if (newsCount == 0)
                            Toast.makeText(activity, "No Local news to show", Toast.LENGTH_LONG)
                    }
                }
            }

        })
    }

    fun getCountryCode(): String {
        return resources.configuration.locale.country.toLowerCase()
    }

}
