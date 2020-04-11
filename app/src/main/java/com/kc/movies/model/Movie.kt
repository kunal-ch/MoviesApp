package com.kc.movies.model

import java.io.Serializable

data class Movie(val id: Long,
                 val title: String,
                 val overview: String,
                 val vote: Float,
                 val releaseDate: String,
                 val backdrop: String?,
                 var poster_path: String?) : Serializable