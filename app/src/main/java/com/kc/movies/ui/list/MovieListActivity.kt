package com.kc.movies.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kc.movies.R
import com.kc.movies.model.Movie
import com.kc.movies.ui.detail.MovieDetailActivity
import com.kc.movies.utils.*
import kotlinx.android.synthetic.main.activity_movie_list.*

private const val SPAN_COUNT = 2

class MovieListActivity : AppCompatActivity(), MovieListAdapter.OnInteractionListener {

  private lateinit var viewModel: MovieListViewModel
  private var movieListAdapter: MovieListAdapter? = null
  var searchView: SearchView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_list)

    setupUI()
    setUpLiveDataListeners()
    checkForConnection()
  }

  private fun setUpLiveDataListeners() {
    viewModel.moviesLiveData.observe(this, Observer { it ->
      homePb?.visibility = View.GONE
      movieListAdapter?.setMovies(it)
      homeRv?.scheduleLayoutAnimation()
    })

    viewModel.errorLiveData.observe(this, Observer { it ->
      homePb.visibility = View.GONE
      showErrorDialog(this,it)
    })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_home, menu)
    val searchMenuItem = menu.findItem(R.id.searchItem)
    searchView = searchMenuItem.actionView as SearchView
    searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }
      override fun onQueryTextChange(newText: String?): Boolean {
        movieListAdapter!!.filter.filter(newText)
        return false
      }
    })
    return true
  }

  private fun setupUI(){
    movieListAdapter = MovieListAdapter(layoutInflater, this)
    viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)

    setSupportActionBar(homeToolbar)
    homeToolbar?.title = ""

    homeRv.setHasFixedSize(true)
    homeRv.layoutManager = GridLayoutManager(this, SPAN_COUNT)
    homeRv.adapter = movieListAdapter

    //hide search keyboard when scrolling
    homeRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        hideKeyboard(this@MovieListActivity)
        super.onScrollStateChanged(recyclerView, newState)
      }
    })
  }

  private fun checkForConnection(){
    if (!isOnline(this)) {
      homePb.visibility = View.GONE
      showAlertDialogForList(this)
    } else {
      homePb.visibility = View.VISIBLE
      viewModel.getMovies()
    }
  }

  override fun onItemClicked(movie: Movie) {
    val bundle = Bundle()
    bundle.putSerializable(Key.MOVIE, movie)
    val intent = Intent(this, MovieDetailActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
    homeToolbar.collapseActionView()
  }
}