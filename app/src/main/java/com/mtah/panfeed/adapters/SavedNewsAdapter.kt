package com.mtah.panfeed.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mtah.panfeed.R
import com.mtah.panfeed.api.GlideApp
import com.mtah.panfeed.models.Article
import org.ocpsoft.prettytime.PrettyTime
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SavedNewsAdapter(): RecyclerView.Adapter<SavedNewsAdapter.SavedNewsHolder>() {
    private var savedNews = listOf<Article>()

    class SavedNewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val title = itemView.findViewById<TextView>(R.id.savedNewsTitle)
        private val image = itemView.findViewById<ImageView>(R.id.savedNewsImage)
        private val date = itemView.findViewById<TextView>(R.id.savedNewsDate)
        var newsUrl: String = ""

        fun bind (article: Article) {
            title.text = article.title
            newsUrl = article.url

            GlideApp.with(itemView)
                .load(article.urlToImage)
                .fallback(R.drawable.no_img)
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .centerCrop()
                .into(image)

            date.text = getDate(getDate(article.publishedAt))
            Log.i("SavedNewsAdapter", "Date ==> ${date.text} ")

        }

        private fun getDate(publishTime: String): String {
            return publishTime.substringBefore("T")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedNewsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_news, parent, false)
        return SavedNewsHolder(itemView)
    }

    override fun onBindViewHolder(holder: SavedNewsHolder, position: Int) {
        holder.bind(savedNews[position])
    }

    override fun getItemCount(): Int {
        return savedNews.size
    }

    fun setNewsList(newsList: List<Article>){
        this.savedNews = newsList
        notifyDataSetChanged()
    }
}