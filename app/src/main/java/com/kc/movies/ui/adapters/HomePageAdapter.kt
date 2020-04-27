package com.kc.movies.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kc.movies.ui.view.FavouriteFragment
import com.kc.movies.ui.view.MovieListFragment

class HomePageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MovieListFragment.newInstance()
            1 -> FavouriteFragment.newInstance()
            else -> MovieListFragment.newInstance()
        }
    }
}