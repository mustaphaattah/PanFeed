package com.mtah.panfeed.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mtah.panfeed.BuildConfig
import com.mtah.panfeed.MainActivity
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

class LocalFragment : Fragment(), NewsAdapter.OnNewsClickListener {

    val API_KEY = BuildConfig.api_key
    val TAG = "LocalFragment"
    val CATEGORY = "health"
    val PAGE_SIZE = 100
    //val  EXTRA_ARTICLE = "com.mtah.panfeed.fragments.EXTRA_ARTICLE"

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var newsAdapter: NewsAdapter
    var articles = arrayListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_local, container, false)


        recyclerView = view.findViewById(R.id.localRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        newsAdapter = NewsAdapter( articles, this)
        recyclerView.adapter = newsAdapter

        swipeRefresh = view.findViewById(R.id.localSwipeRefresh)
        swipeRefresh.setOnRefreshListener { fetchLocalNews() }

        fetchLocalNews()

        return view
    }


    private fun fetchLocalNews(){
        swipeRefresh.isRefreshing = true
        val request =
            NewsApiClient.getApi(NewsInterface::class.java)
        val country = getCountryCode()
        val call = request.getLocalNews(API_KEY, country, CATEGORY, PAGE_SIZE)
        Log.d(TAG, "the locale code: $country")

        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                Toast.makeText(activity, "Unable to get Local news", Toast.LENGTH_SHORT)
                Log.e(TAG, "Unable to get Local news")
                Log.e(TAG, "Error: ${t.message}")
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                swipeRefresh.isRefreshing = false
                if (response.isSuccessful){

                    articles = response.body()!!.articles as ArrayList<Article>
                    newsAdapter.addAllArticles(articles)


                    val newsCount = recyclerView.adapter!!.itemCount
                    Log.d(TAG, "onResponse successful:  Done! got $newsCount articles")
                    if (newsCount == 0)
                        Toast.makeText(activity, "No Local news to show", Toast.LENGTH_LONG)
                }
                else {
                    response.raw().body?.close()
                }
//                response.raw().body?.close()
            }

        })
    }

    private fun getCountryCode(): String {
        return resources.configuration.locales[0].country.toLowerCase()
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
