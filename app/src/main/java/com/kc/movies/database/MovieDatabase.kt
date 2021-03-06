package com.kc.movies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kc.movies.model.Movie

@Database(entities = [(Movie::class)], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}