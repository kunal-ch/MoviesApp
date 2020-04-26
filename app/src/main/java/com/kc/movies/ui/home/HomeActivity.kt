package com.kc.movies.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kc.movies.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpUI()
    }

    private fun setUpUI() {
        setSupportActionBar(homeToolbar)
        homeToolbar?.title = ""
        //homeToolbar.collapseActionView()

        viewpager.adapter = HomePageAdapter(this)
        val tabLayoutMediator = TabLayoutMediator(tabLayout, viewpager, object: TabLayoutMediator.TabConfigurationStrategy{
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                when(position){
                    0 -> tab.text = "All"
                    1 -> tab.text = "Favourite"
                }
            }
        })
        tabLayoutMediator.attach()
    }
}
