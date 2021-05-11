package io.hung.vseecodechallenge.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.hung.vseecodechallenge.model.News
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