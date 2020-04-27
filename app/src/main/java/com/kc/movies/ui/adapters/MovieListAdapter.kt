package com.kc.movies.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kc.movies.R
import com.kc.movies.listener.RecyclerViewItemClickListener
import com.kc.movies.listener.RecyclerViewItemCheckListener
import com.kc.movies.loadImage
import com.kc.movies.model.Movie
import com.kc.movies.utils.getPosterUrl
import java.util.*
import kotlin.collections.ArrayList

class MovieListAdapter(private val inflater: LayoutInflater,
                       private val recyclerViewItemClickListener: RecyclerViewItemClickListener,
                       private val onItemCheckListener: RecyclerViewItemCheckListener
) : RecyclerView.Adapter<MovieListAdapter.HomeViewHolder>(), Filterable {

    private var movies = arrayListOf<Movie>()
    private var moviesFiltered = arrayListOf<Movie>()
    private var favMovieIds = arrayListOf<Long>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            HomeViewHolder(
                    inflater.inflate(R.layout.card_home, parent, false),
                    recyclerViewItemClickListener, onItemCheckListener
            )

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(moviesFiltered[position], favMovieIds)
    }

    override fun getItemId(position: Int): Long {
        return moviesFiltered[position].id!!
    }

    override fun getItemCount() = moviesFiltered.size

    // For Movie List Fragment
    fun setMovies(latest_movies: List<Movie>) {
        val positionStart = moviesFiltered.size
        this.movies.addAll(latest_movies)
        this.moviesFiltered.addAll(latest_movies)
        notifyItemRangeInserted(positionStart, moviesFiltered.size)
    }

    fun getMovies() : List<Movie> {
        return this.moviesFiltered
    }

    // For Favourite Fragment
    fun setNewMovies(new_movies: List<Movie>) {
        this.movies.clear()
        this.moviesFiltered.clear()
        this.movies.addAll(new_movies)
        this.moviesFiltered.addAll(new_movies)
        notifyDataSetChanged()
    }

    // Set Favourite Movie Ids
    fun setFavMovieIds(fav_ids: ArrayList<Long>){
        favMovieIds = fav_ids
    }

    class HomeViewHolder(itemView: View, private val recyclerViewItemClickListener: RecyclerViewItemClickListener, private val onItemCheckListener: RecyclerViewItemCheckListener)
        : RecyclerView.ViewHolder(itemView) {

        private val posterIv: ImageView = itemView.findViewById(R.id.homePosterIv)
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(movie: Movie, favMovieIds: ArrayList<Long>) {
            posterIv.apply {
                loadImage(getPosterUrl(movie.posterPath))
                setOnClickListener {
                    recyclerViewItemClickListener.onItemClicked(movie)
                }
            }

            checkbox.isChecked = favMovieIds.contains(movie.id)

            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView?.isPressed!!)
                    onItemCheckListener.onItemChecked(isChecked, movie)
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