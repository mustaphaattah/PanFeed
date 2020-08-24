package com.mtah.panfeed.persistence

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mtah.panfeed.models.Article
import com.mtah.panfeed.models.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class NewsDatabase: RoomDatabase() {

    abstract fun savedNewsDao(): SavedNewsDao

    private class NewsDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val newsDao = database.savedNewsDao()
                    newsDao.deleteAll()

                    newsDao.insert( Article(
                        "Chainlink (LINK) Could Soon Reverse Towards $14 Despite 20% Price Bounce",
                        "https://bitcoinist.com/chainlink-link-could-soon-reverse-towards-14-despite-20-price-bounce/",
                        "https://bitcoinist.com/wp-content/uploads/2020/05/bradley-dunn-cpHiVhybM6w-unsplash-1920x1282.jpg",
                        "2020-08-24T20:30:00Z"
                    ))
                    newsDao.insert( Article(
                        "Akon Will Manage The Presidential Campaign Of ‘Mighty Ducks’ Actor And Bitcoin Entrepreneur Brock Pierce",
                        "https://uproxx.com/music/akon-brock-pierce-bitcoin-presidential-campaign/",
                        "https://uproxx.com/wp-content/uploads/2020/08/akon-grid.jpg?w=710",
                        "2020-08-24T20:04:28Z"
                    ))

                    Log.i("NewsDatabaseCallback", "Database preloaded")
                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context,
                        scope: CoroutineScope): NewsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_database"
                ).addCallback(NewsDatabaseCallback(scope)).build()

                INSTANCE = instance
                return instance
            }

        }
    }
}