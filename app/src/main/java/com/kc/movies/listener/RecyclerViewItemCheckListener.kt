package com.kc.movies.listener

import com.kc.movies.model.Movie

interface RecyclerViewItemCheckListener {
    fun onItemChecked(isCheck: Boolean, movie: Movie)
}