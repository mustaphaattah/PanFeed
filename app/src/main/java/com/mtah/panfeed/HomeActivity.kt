package com.mtah.panfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mtah.panfeed.model.Article
import com.mtah.panfeed.model.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {


    val TAG = "INFO"
    val COVID_KEYWORD = "covid19"

    var newArticles = listOf<Article>()
   // val recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter

        var request = NewsApiClient.getApi(NewsInterface::class.java)
        var call = request.getAllCovidNews(getString(R.string.api_key), COVID_KEYWORD)

        Log.i(TAG, "Sending requests...")
        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.e(TAG, "Response Failure")
                Toast.makeText(this@HomeActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                Log.e(TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful){
                    Log.i(TAG, "onResponse successful: Showing articles")

                    recyclerView.apply {
                        //layoutManager = LinearLayoutManager(this@HomeActivity)
                        itemAnimator = DefaultItemAnimator()
                        adapter = NewsAdapter(response.body()!!.articles)
                        Log.i(TAG, "onResponse successful:  Done! got ${(adapter as NewsAdapter).itemCount} articles")
                    }
                }
            }

        })
    }
}
