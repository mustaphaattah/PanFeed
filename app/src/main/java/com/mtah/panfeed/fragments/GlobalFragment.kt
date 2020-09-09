package com.mtah.panfeed.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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
import java.util.*
import kotlin.collections.ArrayList

class GlobalFragment : Fragment(), NewsAdapter.OnNewsClickListener {

    private val APIKEY = BuildConfig.api_key
    private val TAG = "GlobalFragment"
    private val COVIDKEYWORD = "coronavirus"
    private val SORT_BY = "publishedAt"
    private val PAGESIZE = 100
    var articles = arrayListOf<Article>()

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_global, container, false)

        recyclerView = view.findViewById(R.id.globalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        newsAdapter = NewsAdapter(mutableListOf(), this)
        recyclerView.adapter = newsAdapter

        swipeRefresh = view.findViewById(R.id.globalRefresh)
        swipeRefresh.setOnRefreshListener { fetchNews() }

        fetchNews()

        return view
    }



    private fun fetchNews(){
        swipeRefresh.isRefreshing = true
        Log.d(TAG, "fetching News")
        val request = NewsApiClient.getApi(NewsInterface::class.java)
        val lang = getLanguage()
        val call = request.getAllCovidNews(APIKEY, COVIDKEYWORD, lang, PAGESIZE, SORT_BY)
        Log.d(TAG, "news language $lang")

        Log.d(TAG, "Sending requests...")
        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                Log.e(TAG, "Response Failure")
                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                swipeRefresh.isRefreshing = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse successful: Showing articles")
                    articles = (response.body()!!.articles as ArrayList<Article>)
//                    filtering out daily mail articles
                    newsAdapter.addAllArticles(articles.filterNot { it.source.name.toLowerCase() == "dailymail" })

                    val newsCount = newsAdapter.itemCount
                    Log.d(TAG, "onResponse successful:  Done! got $newsCount articles")
                    if (newsCount == 0)
                        Toast.makeText(activity, "No Global news to show", Toast.LENGTH_LONG)
                }
                else {
                    response.raw().body?.close()
                }
            }
        })

    }

    private fun getLanguage() : String{
        return Locale.getDefault().language
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
