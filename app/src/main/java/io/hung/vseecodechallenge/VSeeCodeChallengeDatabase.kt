package io.hung.vseecodechallenge

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [News::class], version = 1)
abstract class VSeeCodeChallengeDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}