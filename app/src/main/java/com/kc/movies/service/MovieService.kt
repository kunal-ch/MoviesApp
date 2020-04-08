package com.kc.movies.service

import com.kc.movies.model.MovieResponse
import com.kc.movies.utils.Constant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("/3/discover/movie")
    fun discover(
            @Query("page") page: Int,
            @Query("api_key") apiKey: String = Constant.API_KEY): Call<MovieResponse>
}