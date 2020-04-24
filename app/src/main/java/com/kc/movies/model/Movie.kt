package com.kc.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kc.movies.model.pages.CreditsPage
import com.kc.movies.model.pages.VideosPage
import java.io.Serializable

@Entity(tableName = "movie")
data class Movie(
        @ColumnInfo(name="id") @PrimaryKey val id: Long? = null,
        @ColumnInfo(name="voteCount") @SerializedName("vote_count") val voteCount: Int? = null,
        @ColumnInfo(name="voteAverage") @SerializedName("vote_average") val voteAverage: Double? = null,
        @ColumnInfo(name="runtime") val runtime: Int? = null,
        @ColumnInfo(name="title") val title: String? = null,
        @ColumnInfo(name="releaseDate") @SerializedName("release_date") val releaseDate: String? = null,
        @ColumnInfo(name="posterPath") @SerializedName("poster_path") val posterPath: String? = null,
        @ColumnInfo(name="backdropPath") @SerializedName("backdrop_path") val backdropPath: String? = null,
        val genres: List<Genre>? = null,
        @ColumnInfo(name="overview") val overview: String? = null,
        val credits: CreditsPage? = null,
        val videos: VideosPage? = null
) : Serializable