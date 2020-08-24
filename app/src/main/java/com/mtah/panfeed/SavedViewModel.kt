package com.mtah.panfeed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mtah.panfeed.models.Article
import com.mtah.panfeed.persistence.NewsDatabase
import com.mtah.panfeed.persistence.NewsRepository
import com.mtah.panfeed.persistence.SavedNewsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedViewModel(application: Application): AndroidViewModel(application) {

    private val repository: NewsRepository
    val savedNews: LiveData<List<Article>>

    init {
        val newsDao = NewsDatabase.getInstance(application, viewModelScope).savedNewsDao()
        repository = NewsRepository(newsDao)
        savedNews = repository.savedNews
    }

    fun insert(article: Article) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(article)
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