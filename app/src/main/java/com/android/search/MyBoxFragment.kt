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
import com.android.search.adapter.MyBoxAdapter
import com.android.search.data.KakaoImage
import com.android.search.databinding.FragmentMyBoxBinding

class MyBoxFragment : Fragment() {
    private var _binding: FragmentMyBoxBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var myItemList: ArrayList<KakaoImage>
    private lateinit var mAdapter : MyBoxAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        myItemList = (mContext as MainActivity).myItemList
        mAdapter = MyBoxAdapter(myItemList)
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

        emptyView()

        binding.myitemsRecyclerview.adapter = mAdapter
        val layoutManaged = GridWrapper(requireContext(),2)
        binding.myitemsRecyclerview.layoutManager = layoutManaged

        clickRemoveItem()
    }

    override fun onResume() {
        super.onResume()
        mAdapter.myItemList = (mContext as MainActivity).myItemList
        //noinspection NotifyDataSetChanged
        mAdapter.notifyDataSetChanged()

        emptyView()
    }

    private fun clickRemoveItem() {
        object : MyBoxAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val del = AlertDialog.Builder(activity as Context)
                del.setIcon(R.mipmap.ic_launcher)
                del.setTitle("리스트에서 삭제")
                del.setMessage("리스트에서 정말 삭제 하시겠습니까?")

                Log.d("MyItemsFragment","#aaa onClick")
                //확인을 누르면 지워지게
                del.setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, which ->
                        var unLikeCheck = (mContext as MainActivity).unLike(myItemList[position])
                        mAdapter.myItemList = (mContext as MainActivity).myItemList
//                        myItemList[position].isLike = false
                        Log.d("MyItemsFragment","#aaa unLikeCheck = $unLikeCheck")
                        //noinspection NotifyDataSetChanged
                        mAdapter.notifyDataSetChanged()
                    })
                del.setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                del.show()
            }
        }.also { mAdapter.itmeClick = it }
    }

    private fun emptyView() {
        if (myItemList.size == 0) {
            binding.tvEmpty.visibility = View.VISIBLE
        }else {
            binding.tvEmpty.visibility = View.INVISIBLE
        }
    }
}
