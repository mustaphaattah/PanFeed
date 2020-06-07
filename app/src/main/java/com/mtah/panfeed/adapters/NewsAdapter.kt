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

class NewsAdapter(var articles: MutableList<Article>,
                  val clickListener: OnNewsClickListener
): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position], clickListener)
    }

    fun addAllArticles(responseArticles: List<Article>) {
        articles.addAll(responseArticles)
        notifyDataSetChanged()
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var newsTitle: TextView = itemView.findViewById(R.id.titleTv)
        private var newsSource: TextView = itemView.findViewById(R.id.sourceTv)
        private var newsDate: TextView = itemView.findViewById(R.id.dateTv)
        private var newsImage: ImageView = itemView.findViewById(R.id.newsImageView)
        var newsUrl: String = ""


        fun bind(article: Article, onClick: OnNewsClickListener) {

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

            itemView.setOnClickListener {
                onClick.onItemClick(article, adapterPosition)
            }


        }

        private fun prettyDate(publishTime: String): String {
            var prettyTime = PrettyTime(Locale.getDefault().country.toLowerCase())
            var time = ""
            try {
                var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
                var date = dateFormat.parse(publishTime)
                time = prettyTime.format(date)
            } catch (e: Exception) {
                Log.e("Adapter", e.localizedMessage)
                e.printStackTrace()
            }
            return time
        }

    }

    interface OnNewsClickListener {
        fun onItemClick(article: Article, position: Int)
    }
}