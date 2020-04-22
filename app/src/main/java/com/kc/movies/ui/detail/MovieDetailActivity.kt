package com.kc.movies.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kc.movies.R
import com.kc.movies.loadImage
import com.kc.movies.model.Movie
import com.kc.movies.utils.*
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity: AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel
    private var movie: Movie? = null
    private var detailsAdapter: MovieDetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        getMovieFromBundle(savedInstanceState)
        setupUI()
        setMovieDetails(movie)
        setUpLiveDataListeners()
        checkForConnection()
    }

    private fun getMovieFromBundle(savedInstanceState: Bundle?) {
        movie = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(Key.MOVIE) as Movie
        } else {
            intent.getSerializableExtra(Key.MOVIE) as Movie
        }
    }

    private fun setUpLiveDataListeners() {
        viewModel.moviesLiveData.observe(this, Observer { it ->
            setMovieDetails(it)
        })

        viewModel.errorLiveData.observe(this, Observer { it ->
            showErrorDialog(this,it)
        })
    }

    private fun checkForConnection(){
        if (!isOnline(this)) {
            showAlertDialogForDetail(this)
        } else {
            viewModel.getMovieDetail(movie?.id)
        }
    }


    private fun setupUI() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_grey)
        clearLightStatusBar(window.decorView)

        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)

        setSupportActionBar(detailsToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            detailsToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            detailsToolbar.setNavigationOnClickListener { finish() }
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            title = ""
        }

        detailsIv.transitionName = getString(R.string.transition_name_details) + movie?.id
        detailsIv.loadImage(getBackdropUrl(movie?.backdropPath))
    }


    override fun onBackPressed() {
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.putSerializable(Key.MOVIE, movie)
        super.onSaveInstanceState(outState)
    }

    private fun setMovieDetails(movie: Movie?) {
        val trailer = movie?.videos?.getYoutubeTrailer()
        if (trailer != null) {
            detailsPlayIv.visibility = View.VISIBLE
            detailsToolbarContainer.setOnClickListener {
                val url = Uri.parse("https://www.youtube.com/watch?v=${trailer.key}")
                startActivity(
                        Intent(Intent.ACTION_VIEW, url)
                )
            }
        } else {
            detailsPlayIv.visibility = View.GONE
        }

        if (detailsAdapter == null) {
            detailsAdapter = MovieDetailAdapter(layoutInflater)
            detailsRv.layoutManager = LinearLayoutManager(this)
            detailsRv.adapter = detailsAdapter
        }

        detailsAdapter?.setMovie(movie)
    }
}