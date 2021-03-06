package com.kc.movies.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kc.movies.R
import com.kc.movies.customviews.EndlessRecyclerViewScrollListener
import com.kc.movies.repository.MovieDatabaseRepository
import com.kc.movies.listener.RecyclerViewItemCheckListener
import com.kc.movies.listener.RecyclerViewItemClickListener
import com.kc.movies.model.Movie
import com.kc.movies.ui.adapters.MovieListAdapter
import com.kc.movies.ui.viewmodels.MovieListViewModel
import com.kc.movies.utils.*

class MovieListFragment : Fragment(), RecyclerViewItemClickListener, RecyclerViewItemCheckListener {

  private lateinit var viewModel: MovieListViewModel
  private lateinit var movieListAdapter: MovieListAdapter
  var pageNo = 1
  private lateinit var movieDatabaseRepository: MovieDatabaseRepository
  private lateinit var mView: View
  private lateinit var homeRv: RecyclerView
  private lateinit var homePb: ProgressBar
  private lateinit var emptyView: TextView
  private lateinit var searchView: SearchView
  private var favMovieIds = ArrayList<Long>()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    mView = inflater.inflate(R.layout.fragment_movie_list, container, false)
    homeRv = mView.findViewById(R.id.homeRv) as RecyclerView
    homePb = mView.findViewById(R.id.homePb) as ProgressBar
    emptyView = mView.findViewById(R.id.empty_view) as TextView
    movieDatabaseRepository = MovieDatabaseRepository(activity!!.applicationContext)

    setHasOptionsMenu(true)
    setupUI()
    setUpLiveDataListeners()
    fetchApi(pageNo, true)

    return mView
  }

  private fun setUpLiveDataListeners() {
    viewModel.moviesLiveData.observe(this, Observer { movies ->
      homePb.visibility = View.GONE
      movieListAdapter.setMovies(movies)
      homeRv.scheduleLayoutAnimation()
      checkForEmptyView()
    })

    viewModel.errorLiveData.observe(this, Observer { errorString ->
      homePb.visibility = View.GONE
      showErrorDialog(activity!!,errorString)
      checkForEmptyView()
    })

    movieDatabaseRepository.getAllFavMoviesIds().observe(this, Observer { ids ->
      favMovieIds.clear()
      favMovieIds.addAll(ids)
      movieListAdapter.setFavMovieIds(favMovieIds)
    })
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_home, menu)
    val searchMenuItem = menu.findItem(R.id.searchItem)
    searchView = searchMenuItem.actionView as SearchView
    searchMenuItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
      override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        Search.isSearching = true
        return true
      }

      override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        Search.isSearching = false
        checkForEmptyView()
        return true
      }
    })
    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        movieListAdapter.filter.filter(newText)
        checkForEmptyView()
        return false
      }
    })
  }

  private fun setupUI() {
    viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
    movieListAdapter = MovieListAdapter(layoutInflater, this, this)
    movieListAdapter.setHasStableIds(true)

    homeRv.setHasFixedSize(true)
    homeRv.itemAnimator = null
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
      checkForEmptyView()
      if (showAlert) showAlertDialogForList(activity!!)
    } else {
      homePb.visibility = View.VISIBLE
      viewModel.getMovies(pageNo)
    }
  }

  private fun checkForEmptyView(){
    if (movieListAdapter.getMovies().isEmpty()){
      emptyView.visibility = View.VISIBLE
      homeRv.visibility = View.GONE
    } else {
      emptyView.visibility = View.GONE
      homeRv.visibility = View.VISIBLE
    }
  }

  override fun onItemClicked(movie: Movie) {
    val bundle = Bundle()
    bundle.putSerializable(Key.MOVIE, movie)
    val intent = Intent(activity, MovieDetailActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }

  override fun onItemChecked(isCheck: Boolean, movie: Movie) {
    if (isCheck) {
      movieDatabaseRepository.insertFavMovie(movie)
    } else {
      movieDatabaseRepository.deleteFavMovie(movie)
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() = MovieListFragment()
  }
}