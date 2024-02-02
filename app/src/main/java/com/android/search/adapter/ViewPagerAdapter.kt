package com.android.search.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.search.MyBoxFragment
import com.android.search.SearchFragment

class ViewPagerAdapter (fragment : FragmentActivity) : FragmentStateAdapter(fragment) {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    val fragments = listOf<Fragment>(SearchFragment(), MyBoxFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {



        return fragments[position]
    }
}