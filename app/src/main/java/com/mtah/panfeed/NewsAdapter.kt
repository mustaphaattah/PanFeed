package com.mtah.panfeed

import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mtah.panfeed.api.GlideApp
import com.mtah.panfeed.model.Article
import kotlinx.android.synthetic.main.item.view.*

class NewsAdapter(private val articles: List<Article>
//                  , private val onClickListener: View.OnClickListener
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
        TODO("onclick listenre for each card")
        TODO("open web activity with news article")
        //holder.cardView.setOnClickListener()
    }

    class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var newsTitle: TextView = itemView.findViewById(R.id.titleTv)
        private var newsSource: TextView = itemView.findViewById(R.id.sourceTv)
        private var newsDate: TextView = itemView.findViewById(R.id.dateTv)
        private var newsPreview: TextView = itemView.findViewById(R.id.previewTV)
        private var newsImage: ImageView = itemView.findViewById(R.id.newsImageView)
        var cardView: CardView = itemView.findViewById(R.id.cardView)


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
            newsPreview.text = article.content
        }
    }
}