package com.example.movieapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWatchlist(entity:WatchlistEntity)

    @Query("DELETE FROM watchlist_table WHERE id=:id And oneMovieId=:oneMovieId")
    fun deleteWatchlist(id:Long,oneMovieId: Int)

    @Query("SELECT * FROM watchlist_table WHERE id=:id")
    fun getWatchlistById(id:Long) :Array<WatchlistEntity>

    @Query("SELECT * FROM watchlist_table WHERE id=:id AND oneMovieId=:oneMovieId")
    fun getWatchlistByIdAndOneMovieId(id:Long,oneMovieId:Int) : Array<WatchlistEntity>
}