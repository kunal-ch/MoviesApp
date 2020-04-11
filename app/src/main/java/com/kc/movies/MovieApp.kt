package com.kc.movies

import android.app.Application
import com.kc.movies.repository.MovieRepository

class MovieApp: Application() {

    /**
     * Provides centralised Repository throughout the app
     */
    fun getMovieRepository() = MovieRepository(this)
}