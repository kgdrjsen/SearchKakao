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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.android.search.adapter.SearchAdapter
import com.android.search.databinding.FragmentSearchBinding
import com.android.search.retrofit.NetWorkClient
import com.android.search.viewmodel.MainViewModel
import com.android.search.viewmodel.ViewModelFactory

const val Tag = "SearchFragment"

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var mAdapter: SearchAdapter
    private val retrofit = NetWorkClient.imgNetWork
//    val mainViewModel by activityViewModels<MainViewModel>() {
//        ViewModelFactory(retrofit)
//    }
    private val mainViewModel by lazy { ViewModelProvider(requireActivity(),ViewModelFactory(retrofit)).get(MainViewModel::class.java) }

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
        clickItem()
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
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

//        binding.recyclerview.adapter = mAdapter
//        mainViewModel._searchResults.observe(viewLifecycleOwner) {
//            Log.d(Tag,"#aaa 들어옴")
//            mAdapter.items = it.toMutableList()
//            //noinspection NotifyDataSetChanged
//            mAdapter.notifyDataSetChanged()
//        }

        binding.recyclerview.itemAnimator = null

        mAdapter.itemClick = clickItem()
    }

    private fun observeViewModel() {
        mainViewModel.searchResult.observe(viewLifecycleOwner) {
            mAdapter.items = it.toMutableList()
            //noinspection NotifyDataSetChanged
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun setOnQueryTextListenter() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.getDataWithKeyword(query ?: "")
                Log.d(Tag, "#aaa search = $query")
                saveData(mContext,query ?: "")
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
            mainViewModel.likeItem(item)
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
