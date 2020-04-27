package com.kc.movies

import android.app.Application
import androidx.room.Room
import com.kc.movies.database.MovieDatabase
import com.kc.movies.repository.MovieRepository

class MovieApp: Application() {

    companion object {
        var database: MovieDatabase? = null
    }

    /**
     * Provides centralised Repository throughout the app
     */
    fun getMovieRepository() = MovieRepository(this)

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, MovieDatabase::class.java,"movie-db").build()
    }
}