package com.kc.movies.repository

import android.app.Application
import android.util.Log
import com.kc.movies.model.Movie
import com.kc.movies.model.MovieResponse
import com.kc.movies.service.ApiClient
import com.kc.movies.service.MovieCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(application: Application) {

    fun getMovies(callback: MovieCallback<Movie>){
        var call = ApiClient.build()?.discover(1)
        call?.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                callback.onError(t.message)
            }
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.body() != null) {
                    val movieResponse:MovieResponse  = response.body()!!
                    Log.d("TAG", "response: $movieResponse")
                    callback.onSuccess(movieResponse.results)
                }
            }
        })
    }

}