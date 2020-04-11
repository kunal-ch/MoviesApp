package com.kc.movies.ui.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.kc.movies.MovieApp
import com.kc.movies.model.Movie
import com.kc.movies.service.MovieCallback

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = getApplication<MovieApp>().getMovieRepository()

    fun getMovies(){
        movieRepository.getMovies(object: MovieCallback<Movie>{
            override fun onSuccess(data: List<Movie>?) {
                Log.d("TAG", "data.count ${data?.size}")
            }

            override fun onError(error: String?) {
                Log.d("TAG", "error $error")
            }
        })
    }
}