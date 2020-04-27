package com.kc.movies.database

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.kc.movies.model.Movie

class MovieDatabaseRepository(val context: Context) {

    private val DB_NAME = "movie-db"
    private var movieDatabase: MovieDatabase
    init {
        movieDatabase = Room.databaseBuilder(context,MovieDatabase::class.java, DB_NAME).build()
    }

    fun insertFavMovie(movie: Movie){
        AsyncTask.execute {
            // check if already exist
            val movieExist: Movie? = movieDatabase.getMovieDao().isAlreadyExist(movie.id!!)
            if (movieExist == null)
                movieDatabase.getMovieDao().insertMovie(movie)
        }
    }

    fun deleteFavMovie(movie: Movie){
        AsyncTask.execute{
            movieDatabase.getMovieDao().deleteMovie(movie)
        }
    }

    fun getAllFavMovies() : LiveData<List<Movie>> {
        return movieDatabase.getMovieDao().getAllMovies()
    }

    fun getAllFavMoviesIds() : LiveData<List<Long>> {
        return movieDatabase.getMovieDao().getAllMovieIds()
    }

}