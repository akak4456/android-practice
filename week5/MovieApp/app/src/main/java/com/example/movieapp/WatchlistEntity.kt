package com.example.movieapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="watchlist_table")
data class WatchlistEntity(
    @PrimaryKey(autoGenerate = true)
    val idx: Int,
    val id:Long,
    val oneMovieId:Int,
    val imageSrc:String,
    val title:String,
    val releaseDate:String,
    val runtime:String
)