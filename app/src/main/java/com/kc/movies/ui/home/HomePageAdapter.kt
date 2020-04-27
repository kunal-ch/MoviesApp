package com.kc.movies.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kc.movies.ui.favourite.FavouriteFragment
import com.kc.movies.ui.list.MovieListFragment

class HomePageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MovieListFragment()
            1 -> FavouriteFragment()
            else -> MovieListFragment()
        }
    }
}