package com.kc.movies.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kc.movies.R
import com.kc.movies.loadImage
import com.kc.movies.model.Movie
import com.kc.movies.utils.Key
import com.kc.movies.utils.clearLightStatusBar
import com.kc.movies.utils.getBackdropUrl
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.activity_movie_list.*

class MovieDetailActivity: AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel
    private var movie: Movie? = null
    private var detailsAdapter: MovieDetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_grey)
        clearLightStatusBar(window.decorView)

        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)

        movie = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(Key.MOVIE) as Movie
        } else {
            intent.getSerializableExtra(Key.MOVIE) as Movie
        }
        setupUI()
        setMovieDetails(movie)
        checkForError()

        viewModel.moviesLiveData.observe(this, Observer { it ->
            setMovieDetails(it)
        })

        viewModel.getMovieDetail(movie?.id)
    }

    private fun setupUI() {
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

    private fun checkForError() {
        viewModel.errorLiveData.observe(this, Observer { it ->
            homePb.visibility = View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setTitle("MoviesApp")
            builder.setMessage(it)
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("OK"){dialogInterface, which ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        })
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