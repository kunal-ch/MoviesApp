package com.kc.movies.ui.list

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kc.movies.R
import com.kc.movies.model.Movie
import com.kc.movies.utils.Key
import com.kc.movies.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_movielist.*

private const val SPAN_COUNT = 2

class MovieListActivity : AppCompatActivity(), MovieListAdapter.OnInteractionListener {

  private lateinit var viewModel: MovieListViewModel
  private var homeRv: RecyclerView? = null
  private var homeToolbar: Toolbar? = null
  private var progressBar: ProgressBar? = null
  private var movieListAdapter: MovieListAdapter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movielist)
    homeRv = findViewById(R.id.homeRv)
    homeToolbar = findViewById(R.id.homeToolbar)
    homeToolbar?.title = ""
    movieListAdapter = MovieListAdapter(layoutInflater, this)
    homeRv?.setHasFixedSize(true)
    homeRv?.layoutManager = GridLayoutManager(this, SPAN_COUNT)
    homeRv?.adapter = movieListAdapter
    homeRv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        hideKeyboard(this@MovieListActivity)
        super.onScrolled(recyclerView, dx, dy)
      }
    })

    //hide search keyboard when scrolling
    homeRv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        hideKeyboard(this@MovieListActivity)
        super.onScrollStateChanged(recyclerView, newState)
      }
    })

    viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)

    viewModel.moviesLiveData.observe(this, Observer { it ->
      homePb?.visibility = View.GONE
      movieListAdapter?.setMovies(it)
      homeRv?.scheduleLayoutAnimation()
    })

    viewModel.getMovies()
  }

  override fun onItemClicked(movie: Movie) {
    val bundle = Bundle()
    bundle.putSerializable(Key.MOVIE, movie)
  }
}
