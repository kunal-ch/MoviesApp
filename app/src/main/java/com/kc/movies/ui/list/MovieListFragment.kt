package com.kc.movies.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kc.movies.R
import com.kc.movies.database.MovieDao
import com.kc.movies.database.MovieDatabase
import com.kc.movies.model.Movie
import com.kc.movies.ui.detail.MovieDetailActivity
import com.kc.movies.utils.*

class MovieListFragment : Fragment(), MovieListAdapter.OnInteractionListener {

  private lateinit var viewModel: MovieListViewModel
  private lateinit var movieListAdapter: MovieListAdapter
  var pageNo = 1

  private lateinit var movieDatabase: MovieDatabase
  private lateinit var movieDao: MovieDao

  private lateinit var mView: View
  private lateinit var homeRv: RecyclerView
  private lateinit var homePb: ProgressBar
  private lateinit var searchView: SearchView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    mView = inflater.inflate(R.layout.fragment_movie_list, container, false)
    homeRv = mView.findViewById(R.id.homeRv) as RecyclerView
    homePb = mView.findViewById(R.id.homePb) as ProgressBar

    setupUI()
    setUpLiveDataListeners()
    fetchApi(pageNo, true)

    return mView
  }

  private fun setUpLiveDataListeners() {
    viewModel.moviesLiveData.observe(this, Observer { it ->
      homePb.visibility = View.GONE
      movieListAdapter.setMovies(it)
      homeRv.scheduleLayoutAnimation()
    })

    viewModel.errorLiveData.observe(this, Observer { it ->
      homePb.visibility = View.GONE
      showErrorDialog(activity!!,it)
    })
  }

  /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
  }*/

  private fun setupUI() {
    viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
    movieListAdapter = MovieListAdapter(layoutInflater, this)
    movieListAdapter.setHasStableIds(true)

    homeRv.setHasFixedSize(true)
    homeRv.layoutManager = GridLayoutManager(activity, 2)
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
    if (!isOnline(activity!!.applicationContext)) {
      homePb.visibility = View.GONE
      if (showAlert) showAlertDialogForList(activity!!)
    } else {
      homePb.visibility = View.VISIBLE
      viewModel.getMovies(pageNo)
    }
  }

  override fun onItemClicked(movie: Movie) {
    val bundle = Bundle()
    bundle.putSerializable(Key.MOVIE, movie)
    val intent = Intent(activity, MovieDetailActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }
}