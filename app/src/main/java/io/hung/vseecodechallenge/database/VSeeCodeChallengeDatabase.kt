package io.hung.vseecodechallenge.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.hung.vseecodechallenge.database.dao.NewsDao
import io.hung.vseecodechallenge.model.News

@Database(entities = [News::class], version = 1)
abstract class VSeeCodeChallengeDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}