package io.hung.vseecodechallenge

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getAllNews(): Flow<List<News>>

    @Insert
    fun addNews(newsList: List<News>)

    @Query("DELETE FROM news")
    fun deleteAllNews()

    @Transaction
    fun updateNewsList(newsList: List<News>) {
        deleteAllNews()
        addNews(newsList)
    }
}