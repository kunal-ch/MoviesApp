package com.kc.movies.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kc.movies.model.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getAllMovies(): List<Movie>

    @Insert
    fun insertMovie(vararg movie: Movie)
}