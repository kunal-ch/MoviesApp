package com.kc.movies.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.kc.movies.R
import com.kc.movies.repository.MovieDatabaseRepository
import com.kc.movies.listener.RecyclerViewItemClickListener
import com.kc.movies.listener.RecyclerViewItemCheckListener
import com.kc.movies.model.Movie
import com.kc.movies.ui.adapters.MovieListAdapter
import com.kc.movies.utils.Key

/* A simple [Fragment] subclass.
* Use the [FavouriteFragment.newInstance] factory method to
* create an instance of this fragment.
*/
class FavouriteFragment : Fragment(), RecyclerViewItemClickListener, RecyclerViewItemCheckListener {

    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var movieDatabaseRepository: MovieDatabaseRepository
    private lateinit var mView: View
    private lateinit var homeRv: RecyclerView
    private lateinit var homePb: ProgressBar
    private lateinit var emptyView: TextView
    private var favMovieIds = ArrayList<Long>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.fragment_movie_list, container, false)
        homeRv = mView.findViewById(R.id.homeRv) as RecyclerView
        homePb = mView.findViewById(R.id.homePb) as ProgressBar
        emptyView = mView.findViewById(R.id.empty_view) as TextView
        movieDatabaseRepository = MovieDatabaseRepository(activity!!.applicationContext)
        setupUI()
        //setUpLiveDataListeners()
        return mView
    }

    private fun setupUI() {
        movieListAdapter = MovieListAdapter(layoutInflater, this, this)
        movieListAdapter.setHasStableIds(true)

        homePb.visibility = View.GONE
        homeRv.setHasFixedSize(true)
        homeRv.itemAnimator = null
        homeRv.layoutManager = GridLayoutManager(activity, 2)
        homeRv.adapter = movieListAdapter
    }

    private fun setUpLiveDataListeners() {
        movieDatabaseRepository.getAllFavMovies().observe(this, object: Observer<List<Movie>> {
            override fun onChanged(t: List<Movie>?) {
                movieListAdapter.setNewMovies(t!!)
                checkForEmptyView()
            }
        })

        movieDatabaseRepository.getAllFavMoviesIds().observe(this, object: Observer<List<Long>> {
            override fun onChanged(ids: List<Long>) {
                favMovieIds.clear()
                favMovieIds.addAll(ids)
                movieListAdapter.setFavMovieIds(favMovieIds)
            }
        })
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

    override fun onResume() {
        super.onResume()
        setUpLiveDataListeners()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }

    override fun onItemClicked(movie: Movie) {
        val bundle = Bundle()
        bundle.putSerializable(Key.MOVIE, movie)
        val intent = Intent(activity, MovieDetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onItemChecked(isCheck: Boolean, movie: Movie) {
        if (!isCheck) {
            movieDatabaseRepository.deleteFavMovie(movie)
        }
    }
}
