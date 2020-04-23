package com.kc.movies.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
  var pageNo = 1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_list)

    setupUI()
    setUpLiveDataListeners()
    fetchApi(pageNo, true)
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
    searchMenuItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
      override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        Search.isSearching = true
        return true
      }

      override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        Search.isSearching = false
        return true
      }
    })
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
    movieListAdapter?.setHasStableIds(true)
    viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)

    setSupportActionBar(homeToolbar)
    homeToolbar?.title = ""

    homeRv.setHasFixedSize(true)
    homeRv.layoutManager = GridLayoutManager(this, SPAN_COUNT)
    homeRv.adapter = movieListAdapter

    val gridLayoutManager = homeRv.layoutManager as GridLayoutManager
    val listener = object: EndlessRecyclerViewScrollListener(gridLayoutManager) {
      override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
        fetchApi(page+1, false)
      }
    }
    homeRv.addOnScrollListener(listener)
  }

  private fun fetchApi(pageNo: Int, showAlert: Boolean){
    if (!isOnline(this)) {
      homePb.visibility = View.GONE
      if (showAlert) showAlertDialogForList(this)
    } else {
      homePb.visibility = View.VISIBLE
      viewModel.getMovies(pageNo)
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