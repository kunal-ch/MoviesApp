package com.kc.movies.model

import com.google.gson.annotations.SerializedName
import com.kc.movies.model.pages.CreditsPage
import com.kc.movies.model.pages.VideosPage
import java.io.Serializable

data class Movie(
        val id: Long? = null,
        @SerializedName("vote_count")
        val voteCount: Int? = null,
        @SerializedName("vote_average")
        val voteAverage: Double? = null,
        val runtime: Int? = null,
        val title: String? = null,
        @SerializedName("release_date")
        val releaseDate: String? = null,
        @SerializedName("poster_path")
        val posterPath: String? = null,
        @SerializedName("backdrop_path")
        val backdropPath: String? = null,
        val genres: List<Genre>? = null,
        val overview: String? = null,
        val credits: CreditsPage? = null,
        val videos: VideosPage? = null
) : Serializable