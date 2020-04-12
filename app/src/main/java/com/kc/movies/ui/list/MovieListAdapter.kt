package com.kc.movies.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kc.movies.R
import com.kc.movies.loadImage
import com.kc.movies.model.Movie
import com.kc.movies.utils.getPosterUrl

class MovieListAdapter (private val inflater: LayoutInflater,
                        private val onInteractionListener: OnInteractionListener
) : RecyclerView.Adapter<MovieListAdapter.HomeViewHolder>() {

    private var movies = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            HomeViewHolder(
                    inflater.inflate(R.layout.card_home, parent, false),
                    onInteractionListener
            )

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun setMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    interface OnInteractionListener {

        fun onItemClicked(movie: Movie)

    }

    class HomeViewHolder(itemView: View, private val onInteractionListener: OnInteractionListener)
        : RecyclerView.ViewHolder(itemView) {

        private val posterIv: ImageView = itemView.findViewById(R.id.homePosterIv)

        fun bind(movie: Movie) {
            posterIv.apply {
                loadImage(getPosterUrl(movie.posterPath))

                setOnClickListener {
                    onInteractionListener.onItemClicked(movie)
                }
            }
        }

    }
}