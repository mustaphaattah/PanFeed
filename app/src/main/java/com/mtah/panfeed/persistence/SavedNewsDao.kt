package com.mtah.panfeed.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mtah.panfeed.models.Article

@Dao
interface SavedNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * FROM news_table")
    fun getAll(): LiveData<List<Article>>

    @Query("DELETE FROM news_table ")
    suspend fun deleteAll()

}