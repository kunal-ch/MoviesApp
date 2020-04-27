package com.kc.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kc.movies.model.pages.CreditsPage
import com.kc.movies.model.pages.VideosPage
import java.io.Serializable

@Entity(tableName = "movie")
data class Movie(
        @ColumnInfo(name="id") @PrimaryKey var id: Long? = null,
        @ColumnInfo(name="voteCount") @SerializedName("vote_count") var voteCount: Int? = null,
        @ColumnInfo(name="voteAverage") @SerializedName("vote_average") var voteAverage: Double? = null,
        @ColumnInfo(name="runtime") var runtime: Int? = null,
        @ColumnInfo(name="title") var title: String? = null,
        @ColumnInfo(name="releaseDate") @SerializedName("release_date") var releaseDate: String? = null,
        @ColumnInfo(name="posterPath") @SerializedName("poster_path") var posterPath: String? = null,
        @ColumnInfo(name="backdropPath") @SerializedName("backdrop_path") var backdropPath: String? = null,
        @Ignore var genres: List<Genre>? = null,
        @ColumnInfo(name="overview") var overview: String? = null,
        @Ignore var credits: CreditsPage? = null,
        @Ignore var videos: VideosPage? = null
) : Serializable