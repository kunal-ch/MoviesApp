package com.kc.movies.ui.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kc.movies.MovieApp
import com.kc.movies.model.Movie
import com.kc.movies.service.MovieCallback

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    val moviesLiveData = MutableLiveData<List<Movie>>()
    val errorLiveData = MutableLiveData<String>()
    private val movieRepository = getApplication<MovieApp>().getMovieRepository()

    fun getMovies(pageNo: Int){
        movieRepository.getMovies(pageNo, object: MovieCallback<Movie>{
            override fun onSuccess(data: List<Movie>?) {
                moviesLiveData.value = data
            }

            override fun onError(error: String?) {
                Log.d("TAG", "error $error")
                errorLiveData.value = error
            }
        })
    }
}