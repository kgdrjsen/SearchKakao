package com.android.search

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.android.search.adapter.MyBoxAdapter
import com.android.search.data.KakaoImage
import com.android.search.databinding.FragmentMyBoxBinding
import com.android.search.retrofit.NetWorkClient
import com.android.search.viewmodel.MainViewModel
import com.android.search.viewmodel.ViewModelFactory

const val TagMB = "MyBoxFragment"

class MyBoxFragment : Fragment() {
    private var _binding: FragmentMyBoxBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var mAdapter: MyBoxAdapter
    private val retrofit = NetWorkClient.imgNetWork
    val mainViewModel by activityViewModels<MainViewModel>() {
        ViewModelFactory(retrofit)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBoxBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = MyBoxAdapter(mContext)
        binding.myitemsRecyclerview.adapter = mAdapter
        mainViewModel._myItmes.observe(viewLifecycleOwner) {
            mAdapter.items = it.toMutableList()
            //noinspection NotifyDataSetChanged
            mAdapter.notifyDataSetChanged()
            Log.d(TagMB,"#aaa _myItems.value = ${mAdapter.items}")
        }
        val layoutManaged = GridWrapper(requireContext(),2)
        binding.myitemsRecyclerview.layoutManager = layoutManaged

//        observeViewModel()
        mAdapter.clickListener = clickItem()
        clickItem()
//        emptyView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun observeViewModel() {
//        mainViewModel._myItmes.observe(viewLifecycleOwner) {
//            mAdapter.items = it.toMutableList()
//            //noinspection NotifyDataSetChanged
//            mAdapter.notifyDataSetChanged()
//        }
//    }

    private fun clickItem() = object : MyBoxAdapter.ItemClickListener  {
        override fun onClick(item: KakaoImage, position: Int) {
            val del = AlertDialog.Builder(activity as Context)
            del.setIcon(R.mipmap.ic_launcher)
            del.setTitle("리스트에서 삭제")
            del.setMessage("리스트에서 정말 삭제 하시겠습니까?")

            Log.d("MyBoxFragment","#aaa onClick")
            //확인을 누르면 지워지게
            del.setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    val item = mAdapter.items[position]
                    mainViewModel._searchResults.value?.map {
                        if (it.url == item.url){
                            it.isLike = false
                        }
                        Log.d(TagMB,"#aaa searchResult like = ${it.isLike})")
                    }
                    item.isLike = false
                    mAdapter.items.remove(item)
                    mainViewModel.myItmes.value?.remove(item)
                    Log.d(TagMB,"#aaa isLike = ${item.isLike}")
                    Log.d(TagMB,"#aaa likeItem = ${mainViewModel.myItmes.value}")
                    mAdapter.notifyDataSetChanged()
                })
            del.setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            del.show()
        }
    }

//    private fun emptyView() {
//        if (mAdapter.items.size == 0 ) {
//            binding.tvEmpty.visibility = View.VISIBLE
//        }else {
//            binding.tvEmpty.visibility = View.INVISIBLE
//        }
//    }
}


