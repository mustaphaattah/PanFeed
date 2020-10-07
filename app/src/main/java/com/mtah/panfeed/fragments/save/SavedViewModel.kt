package com.mtah.panfeed.fragments.save

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mtah.panfeed.models.Article
import com.mtah.panfeed.persistence.NewsDatabase
import com.mtah.panfeed.persistence.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedViewModel(application: Application): AndroidViewModel(application) {

    private val TAG = "SavedViewModel"
    private val repository: NewsRepository
    private val savedNews: LiveData<List<Article>>

    init {
        val newsDao = NewsDatabase.getInstance(application).savedNewsDao()
        repository = NewsRepository(newsDao)
        savedNews = repository.savedNews
    }

    fun insert(article: Article) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(article)
        Log.d(TAG, "inserting into Room")
    }

    fun delete(article: Article) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(article)
    }


    fun getAll(): LiveData<List<Article>> {
        return repository.getAll()
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }


}