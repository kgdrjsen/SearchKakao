package com.android.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.search.adapter.SearchAdapter
import com.android.search.data.KakaoImage
import com.android.search.databinding.FragmentSearchBinding
import com.android.search.retrofit.NetWorkClient
import com.android.search.retrofit.NetWorkInterface
import com.android.search.viewmodel.SearchViewModel

//class SearchFragment : Fragment() {
//
//    private var _binding : FragmentSearchBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var mContext: Context
//    private var searchItems : ArrayList<KakaoImage> = ArrayList()
//    private var receiveItems : MutableList<Image.Documents> = mutableListOf()
//    private val mAdapter by lazy { SearchAdapter(searchItems,mContext) }
//
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mContext = context
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentSearchBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val toolbar = binding.toolbar
//        val activity = activity as AppCompatActivity?
//        activity?.setSupportActionBar(toolbar)
//
//        val query = loadData(mContext)
//        binding.searchView.setQuery(query,false)
//
//        listInit()
//        setOnQueryTextListenter()
//        clickItem()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mAdapter.items = searchItems
//        Log.d("SearchFragment","#aaa onResume = ${searchItems}")
//        //noinspection NotifyDataSetChanged
//        mAdapter.notifyDataSetChanged()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        //데이터 누수 방지
//        _binding = null
//    }
//
//    private fun listInit() {
//        //어댑터연결
//        binding.recyclerview.adapter = mAdapter
//        val layoutManaged = GridWrapper(requireContext(),2)
//        binding.recyclerview.layoutManager = layoutManaged
//        mAdapter.itemClick = clickItem()
//    }
//
//    private fun clickItem() = object : SearchAdapter.ItemClick  {
//        override fun onClick(view: View, position: Int) {
//            var myItemList = (mContext as MainActivity).myItemList
//
//            val site = searchItems[position].site
//            val time = searchItems[position].time
//            val url = searchItems[position].imageUrl
//            val item = KakaoImage(site,time,url)
//            if (!myItemList.contains(item)) {
//                (mContext as MainActivity).like(item)
////                searchItems[position].isLike = true
//                mAdapter.ItemViewHolder(binding = ListItemBinding.bind(view)).like.visibility = View.VISIBLE
//            }else {
//                (mContext as MainActivity).unLike(item)
////                searchItems[position].isLike = false
//                mAdapter.ItemViewHolder(binding = ListItemBinding.bind(view)).like.visibility = View.INVISIBLE
//            }
//        }
//    }
//
//    //api요청
//    private fun responseNetWork(search : String) = lifecycleScope.launch() {
//        val restApi = "2a9592d47e0656c9dcfa6cb012cebc68"
//        val authorization = "KakaoAK $restApi"
//        val responseData = NetWorkClient.imgNetWork.image_search(authorization, search, "accuracy",40,40)
//
//        receiveItems = responseData.documents.toMutableList()
//        receiveItems.forEach {
//            val receiveKakao = KakaoImage(it.display_sitename,it.datetime,it.thumbnail_url)
//            searchItems.add(receiveKakao)
//        }
//
//        withContext(Dispatchers.Main) {
//            mAdapter.items = searchItems
//            //noinspection NotifyDataSetChanged
//            binding.recyclerview.adapter?.notifyDataSetChanged()
//        }
//    }
//
//    private fun setOnQueryTextListenter() {
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//                responseNetWork(query ?: "")
//                Log.d("SearchFragment", "#aaa search = $query")
//                saveData(mContext,query ?: "")
//                Log.d("SearchFragment","#aaa saveData = ${saveData(mContext,query ?:"")}")
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//            }
//        })
//    }
//
//    private fun saveData(context: Context, searchText : String) {
//        mContext=context
//        val pref = mContext.getSharedPreferences("pref",Context.MODE_PRIVATE).edit()
//        pref.putString("name",searchText)
//        pref.apply()
//    }
//
//    private fun loadData(context: Context) : String? {
//        mContext=context
//        val pref = mContext.getSharedPreferences("pref",Context.MODE_PRIVATE)
//        return pref.getString("name","")
//    }
//}

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var mAdapter: SearchAdapter
    private val retrofit = NetWorkClient.imgNetWork
    private val viewModel = SearchViewModel(retrofit)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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

        viewInit()
        setOnQueryTextListenter()
        observeViewModel()
        clickItem()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun viewInit() {
        //어댑터연결
        val layoutManaged = GridWrapper(requireContext(), 2)
        binding.recyclerview.layoutManager = layoutManaged

        mAdapter = SearchAdapter(mContext)

        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.itemAnimator = null

        mAdapter.itemClick = clickItem()
    }

    private fun observeViewModel() {
        viewModel.searchResult.observe(viewLifecycleOwner) {
            mAdapter.items = it.toMutableList()
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun setOnQueryTextListenter() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getDataWithKeyword(query ?: "")
                Log.d("SearchFragment", "#aaa search = $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun clickItem() = object : SearchAdapter.ItemClick  {
        override fun onClick(view: View, position: Int) {
            val item = mAdapter.items[position]
            item.isLike = !item.isLike
            Log.d("SearchFragment","#aaa isLike = ${item.isLike}")

            mAdapter.notifyItemChanged(position)
        }
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
