package com.mtah.panfeed.fragments

import android.content.Intent
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
import com.mtah.panfeed.BuildConfig
import com.mtah.panfeed.adapters.NewsAdapter
import com.mtah.panfeed.api.NewsApiClient
import com.mtah.panfeed.api.NewsInterface
import com.mtah.panfeed.R
import com.mtah.panfeed.ReadActivity
import com.mtah.panfeed.models.Article
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
class GlobalFragment : Fragment(), NewsAdapter.OnNewsClickListener {

    private val API_KEY = BuildConfig.api_key
    private val TAG = "GlobalFragment"
    private val COVID_KEYWORD = "coronavirus"
    private val SORT_BY = "publishedAt"
    private val PAGE_SIZE = 50

    private lateinit var newsAdapter: NewsAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout
    var articles = mutableListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_global, container, false)

        recyclerView = view.findViewById(R.id.globalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        newsAdapter = NewsAdapter(articles, this)
        recyclerView.adapter = newsAdapter

        swipeRefresh = view.findViewById(R.id.globalRefresh)
        swipeRefresh.setOnRefreshListener { fetchNews() }

        //fill the recycler view with news cards
        fetchNews()

        return view
    }

    private fun fetchNews(){
        swipeRefresh.isRefreshing = true
        Log.d(TAG, "fetching News")
        val request =
            NewsApiClient.getApi(NewsInterface::class.java)
        val lang = getLanguage()
        val call = request.getAllCovidNews(API_KEY, COVID_KEYWORD, lang, PAGE_SIZE, SORT_BY)
        Log.d(TAG, "news language $lang")

        Log.d(TAG, "Sending requests...")
        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                Log.e(TAG, "Response Failure")
                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT)
                Log.e(TAG, t.message)
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                swipeRefresh.isRefreshing = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse successful: Showing articles")

                    newsAdapter.addAllArticles(response.body()!!.articles)

                    val newsCount = newsAdapter.itemCount
                    Log.d(TAG, "onResponse successful:  Done! got $newsCount articles")
                    if (newsCount == 0)
                        Toast.makeText(activity, "No Global news to show", Toast.LENGTH_LONG)
                }
            }
        })

    }

    private fun getLanguage() : String{
        return Locale.getDefault().language
    }

    override fun onItemClick(article: Article, position: Int) {
//        TODO("Not yet implemented")
        var readIntent = Intent(context, ReadActivity::class.java)
        readIntent.putExtra("title", article.title)
        readIntent.putExtra("url", article.url)
        readIntent.putExtra("image", article.urlToImage)
        Log.d(TAG, "Article Clicked: ${articles[position]}")
        startActivity(readIntent)
    }
}
