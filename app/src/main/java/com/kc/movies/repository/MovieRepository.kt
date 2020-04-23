package com.kc.movies.repository

import android.app.Application
import android.util.Log
import com.kc.movies.model.Movie
import com.kc.movies.model.MovieResponse
import com.kc.movies.service.ApiClient
import com.kc.movies.service.MovieCallback
import com.kc.movies.service.MovieDetailCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(application: Application) {

    fun getMovies(pageNo: Int, callback: MovieCallback<Movie>){
        var call = ApiClient.build()?.getTopRatedMovies(pageNo)
        call?.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                callback.onError(t.message)
            }
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.body() != null) {
                    val movieResponse:MovieResponse  = response.body()!!
                    callback.onSuccess(movieResponse.results)
                }
            }
        })
    }

    fun getMovieDetail(id: Long, callback: MovieDetailCallback<Movie>){
        var call = ApiClient.build()?.getMovieDetails(id)
        call?.enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                callback.onError(t.message)
            }
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.body() != null) {
                    val movieResponse:Movie  = response.body()!!
                    callback.onSuccess(movieResponse)
                }
            }
        })
    }
}