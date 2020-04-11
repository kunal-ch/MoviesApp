package com.kc.movies.utils

private const val SECURE_BASE_URL = "https://image.tmdb.org/t/p/"
private const val POSTER_PATH = "w300/"

fun getPosterUrl(url: String?): String {
    return if (url != null) {
        SECURE_BASE_URL + POSTER_PATH + url
    } else {
        ""
    }
}