package com.android.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.search.adapter.SearchAdapter
import com.android.search.data.Document
import com.android.search.data.KakaoImage
import com.android.search.databinding.FragmentSearchBinding
import com.android.search.databinding.ListItemBinding
import com.android.search.retrofit.NetWorkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private var searchItems : ArrayList<KakaoImage> = ArrayList()
    private var receiveItems : MutableList<Document> = mutableListOf()
    private val mAdapter by lazy { SearchAdapter(searchItems,mContext) }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)

        val query = loadData(mContext)
        binding.searchView.setQuery(query,false)

        listInit()
        setOnQueryTextListenter()
        clickItem()
    }

    override fun onResume() {
        super.onResume()
        mAdapter.items = searchItems
        Log.d("SearchFragment","#aaa onResume = ${searchItems}")
        //noinspection NotifyDataSetChanged
        mAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //데이터 누수 방지
        _binding = null
    }

    private fun listInit() {
        //어댑터연결
        binding.recyclerview.adapter = mAdapter
        val layoutManaged = GridWrapper(requireContext(),2)
        binding.recyclerview.layoutManager = layoutManaged
        mAdapter.itemClick = clickItem()
    }

    private fun clickItem() = object : SearchAdapter.ItemClick  {
        override fun onClick(view: View, position: Int) {
            var myItemList = (mContext as MainActivity).myItemList

            val site = searchItems[position].site
            val time = searchItems[position].time
            val url = searchItems[position].imageUrl
            val item = KakaoImage(site,time,url)
            if (!myItemList.contains(item)) {
                (mContext as MainActivity).like(item)
//                searchItems[position].isLike = true
                mAdapter.ItemViewHolder(binding = ListItemBinding.bind(view)).like.visibility = View.VISIBLE
            }else {
                (mContext as MainActivity).unLike(item)
//                searchItems[position].isLike = false
                mAdapter.ItemViewHolder(binding = ListItemBinding.bind(view)).like.visibility = View.INVISIBLE
            }
        }
    }

    //api요청
    private fun responseNetWork(search : String) = lifecycleScope.launch() {
        val restApi = "2a9592d47e0656c9dcfa6cb012cebc68"
        val authorization = "KakaoAK $restApi"
        val responseData = NetWorkClient.imgNetWork.getImg(authorization, search, "accuracy")

        receiveItems = responseData.documents.toMutableList()
        receiveItems.forEach {
            val receiveKakao = KakaoImage(it.display_sitename,it.datetime,it.thumbnail_url)
            searchItems.add(receiveKakao)
        }

        withContext(Dispatchers.Main) {
            mAdapter.items = searchItems
            //noinspection NotifyDataSetChanged
            binding.recyclerview.adapter?.notifyDataSetChanged()
        }
    }

    private fun setOnQueryTextListenter() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                responseNetWork(query ?: "")
                Log.d("SearchFragment", "#aaa search = $query")
                saveData(mContext,query ?: "")
                Log.d("SearchFragment","#aaa saveData = ${saveData(mContext,query ?:"")}")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun saveData(context: Context, searchText : String) {
        mContext=context
        val pref = mContext.getSharedPreferences("pref",Context.MODE_PRIVATE).edit()
        pref.putString("name",searchText)
        pref.apply()
    }

    private fun loadData(context: Context) : String? {
        mContext=context
        val pref = mContext.getSharedPreferences("pref",Context.MODE_PRIVATE)
        return pref.getString("name","")
    }
}