package com.mtah.panfeed.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mtah.panfeed.R
import com.mtah.panfeed.ReadActivity
import com.mtah.panfeed.api.GlideApp
import com.mtah.panfeed.models.Article
import org.ocpsoft.prettytime.PrettyTime
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(private val articles: List<Article>
                  , private val context: Context
): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
        holder.cardView.setOnClickListener {
            if (holder.newsUrl.isNullOrEmpty()) {
                Toast.makeText(context, "Cannot open this news link", Toast.LENGTH_SHORT)
            } else {
                var readIntent = Intent(context, ReadActivity::class.java)
                readIntent.putExtra("url", holder.newsUrl)
                context.startActivity(readIntent)
            }
        }
    }

    class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var newsTitle: TextView = itemView.findViewById(R.id.titleTv)
        private var newsSource: TextView = itemView.findViewById(R.id.sourceTv)
        private var newsDate: TextView = itemView.findViewById(R.id.dateTv)
        private var newsImage: ImageView = itemView.findViewById(R.id.newsImageView)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
        var newsUrl: String = ""


        fun bind(article: Article) {

            GlideApp.with(itemView)
                .load(article.urlToImage)
                .fallback(R.drawable.no_img)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.no_img)
                .centerCrop()
                .into(newsImage)

            newsTitle.text = article.title
            newsSource.text = article.source.name
            newsDate.text = article.publishedAt
            newsDate.text = prettyDate(article.publishedAt)
            newsUrl = article.url
        }

        fun prettyDate(publishTime: String): String{
            var prettyTime: PrettyTime = PrettyTime(Locale.getDefault().country.toLowerCase())
            var time: String = ""
            try {
                var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
                var date = dateFormat.parse(publishTime)
                time = prettyTime.format(date)
            } catch (e: Exception){
                Log.e("Adapter", e.localizedMessage)
                e.printStackTrace()
            }
            return time
        }

    }

}