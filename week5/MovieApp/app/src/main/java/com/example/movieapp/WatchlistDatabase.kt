package com.example.movieapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [WatchlistEntity::class], version = 1, exportSchema = false)
abstract class WatchlistDatabase : RoomDatabase() {
    abstract fun watchlistDao() : WatchlistDao

    companion object {
        private var INSTANCE : WatchlistDatabase? = null

        fun getInstance(context: Context) : WatchlistDatabase? {
            if (INSTANCE == null) {
                synchronized(WatchlistDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        WatchlistDatabase::class.java, "memo-db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}