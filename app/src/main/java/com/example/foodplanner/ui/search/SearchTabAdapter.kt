package com.example.foodplanner.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchTabAdapter(
    private val titles: List<String>
) : FragmentStateAdapter(
    FragmentActivity()
) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return SearchFragment()
    }
}