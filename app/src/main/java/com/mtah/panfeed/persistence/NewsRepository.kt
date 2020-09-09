package com.mtah.panfeed.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import com.mtah.panfeed.models.Article

class NewsRepository(private val newsDao: SavedNewsDao) {
    val savedNews: LiveData<List<Article>> = newsDao.getAll()
    private val TAG = "NewsRepository"

    suspend fun insert(article: Article) {
        newsDao.insert(article)
        Log.i(TAG, "insert: repository")
    }

    suspend fun delete(article: Article) {
        newsDao.delete(article)
    }

    fun  getAll(): LiveData<List<Article>> {
        return newsDao.getAll()
    }
    suspend fun deleteAll() {
        newsDao.deleteAll()
    }

}