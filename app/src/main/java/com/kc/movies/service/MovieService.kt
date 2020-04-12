package com.kc.movies.service

import com.kc.movies.model.Movie
import com.kc.movies.model.MovieResponse
import com.kc.movies.utils.Constant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/top_rated")
    fun getTopRatedMovies(
            @Query("page") page: Int,
            @Query("api_key") apiKey: String = Constant.API_KEY): Call<MovieResponse>

    @GET("movie/{movie_id}?append_to_response=credits,videos")
    fun getMovieDetails(@Path(value = "movie_id")movieId: Long,
                        @Query("api_key") apiKey: String = Constant.API_KEY): Call<Movie>
}