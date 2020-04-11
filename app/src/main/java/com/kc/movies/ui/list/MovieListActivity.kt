package com.kc.movies.ui.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.kc.movies.R

class MovieListActivity : AppCompatActivity() {

  private lateinit var viewModel: MovieListViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
    viewModel.getMovies()
  }
}
