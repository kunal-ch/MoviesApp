package com.kc.movies.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kc.movies.MovieApp
import com.kc.movies.model.Movie
import com.kc.movies.service.MovieDetailCallback

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

    val moviesLiveData = MutableLiveData<Movie>()
    val errorLiveData = MutableLiveData<String>()
    private val movieRepository = getApplication<MovieApp>().getMovieRepository()

    fun getMovieDetail(id: Long?) {
        movieRepository.getMovieDetail(id!!, object : MovieDetailCallback<Movie> {
            override fun onSuccess(data: Movie?) {
                moviesLiveData.value = data
            }

            override fun onError(error: String?) {
                Log.d("TAG", "error $error")
                errorLiveData.value = error
            }
        })
    }
}