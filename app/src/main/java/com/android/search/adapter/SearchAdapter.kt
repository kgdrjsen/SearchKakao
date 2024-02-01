package com.android.search.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.search.MainActivity
import com.android.search.data.KakaoImage
import com.android.search.databinding.ListItemBinding
import com.bumptech.glide.Glide

class SearchAdapter (var items : ArrayList<KakaoImage>, val context: Context) : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        Log.d("SearchAdapter", "#aaa onCreateViewHolder")
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it,position)
            Log.d("SearchAdapter","#aaa click? = $position")
        }
        viewBind(holder, position)
    }

    private fun viewBind(holder: ItemViewHolder, position: Int) {

        //섬네일 url받아오기
        Glide.with(holder.itemView.context)
            .load(items[position].imageUrl)
            .into(holder.img)
        //datetime format이 안되서 substring으로 0~19번째까지 나타내고, 불필요한 T를 replace로 없애고 공백으로 바꾸기
        holder.date.text = items[position].time
            .substring(0,19)
            .replace("T"," ")
        holder.site.text = items[position].site

        var myList = (context as MainActivity).myItemList
        if (myList.contains(items[position])) {
            holder.like.visibility = View.VISIBLE
            Log.d("SearchAdapter","#aaa cotain = ${items[position]}")
        }else{
            holder.like.visibility = View.INVISIBLE
        }

//        if (items[position].isLike) {
//            holder.like.visibility = View.VISIBLE
//        }else{
//            holder.like.visibility = View.INVISIBLE
//        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ItemViewHolder (binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.ivItem
        val site = binding.tvImgSite
        val date = binding.tvDate
        val like = binding.ivFavorite
    }
}