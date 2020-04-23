package com.kc.movies.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kc.movies.R
import com.kc.movies.loadImage
import com.kc.movies.model.Movie
import com.kc.movies.utils.getPosterUrl
import java.util.*
import kotlin.collections.ArrayList

class MovieListAdapter (private val inflater: LayoutInflater,
                        private val onInteractionListener: OnInteractionListener
) : RecyclerView.Adapter<MovieListAdapter.HomeViewHolder>(), Filterable {

    private var movies = arrayListOf<Movie>()
    private var moviesFiltered = arrayListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            HomeViewHolder(
                    inflater.inflate(R.layout.card_home, parent, false),
                    onInteractionListener
            )

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(moviesFiltered[position])
    }

    override fun getItemId(position: Int): Long {
        return moviesFiltered[position].id!!
    }

    override fun getItemCount() = moviesFiltered.size

    fun setMovies(latest_movies: List<Movie>) {
        val positionStart = moviesFiltered.size
        this.movies.addAll(latest_movies)
        this.moviesFiltered.addAll(latest_movies)
        notifyItemRangeInserted(positionStart, moviesFiltered.size)
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    moviesFiltered = movies
                } else {
                    val resultList = ArrayList<Movie>()
                    for (row in movies) {
                        if (row.title?.toLowerCase(Locale.ROOT)?.contains(charSearch.toLowerCase(Locale.ROOT))!!) {
                            resultList.add(row)
                        }
                    }
                    moviesFiltered = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = moviesFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                moviesFiltered = results?.values as ArrayList<Movie>
                notifyDataSetChanged()
            }
        }
    }

}