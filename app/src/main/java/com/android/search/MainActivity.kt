package com.android.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.search.adapter.ViewPagerAdapter
import com.android.search.data.KakaoImage
import com.android.search.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val tabTextList = listOf("검색","내 보관함")
    private val tabIconList = listOf(R.drawable.search,R.drawable.youtube)

    var myItemList : ArrayList<KakaoImage> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewPager()
    }

    private fun initViewPager() {
        val viewPager = binding.viewpager
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tablayout,binding.viewpager) {tab, position ->
            tab.text = tabTextList[position]
            tab.setIcon(tabIconList[position])
        }.attach()
    }

    //좋아요 기능
    fun like(item : KakaoImage) {
        if (!myItemList.contains(item)) {
            myItemList.add(item)
            Log.d("MainActivity","#aaa like")
        }
    }
    fun unLike(item: KakaoImage) {
        if (myItemList.contains(item)) {
            myItemList.remove(item)
            Log.d("MainActivity", "#aaa unLike")
        }
//        myItemList.remove(item)
//        Log.d("MainActivity", "#aaa unLike")
    }
}