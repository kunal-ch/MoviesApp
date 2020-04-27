package com.kc.movies.listener

import com.kc.movies.model.Movie

interface RecyclerViewItemClickListener {
    fun onItemClicked(movie: Movie)
}