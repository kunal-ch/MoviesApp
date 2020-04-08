package com.kc.movies.model

import java.io.Serializable

data class MovieResponse(val page: Int?,
                         val totalPages: Int?,
                         val results: List<Movie>?) : Serializable