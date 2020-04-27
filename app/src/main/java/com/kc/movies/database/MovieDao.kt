package com.kc.movies.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kc.movies.model.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun isAlreadyExist(movieId: Long) : Movie

    @Insert
    fun insertMovie(vararg movie: Movie)

    @Delete
    fun deleteMovie(vararg movie: Movie)

    @Query("SELECT id FROM movie")
    fun getAllMovieIds() : LiveData<List<Long>>
}