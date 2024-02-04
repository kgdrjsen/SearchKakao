package com.android.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.search.data.Constants
import com.android.search.data.KakaoImage
import com.android.search.data.Utils.setTime
import com.android.search.databinding.ListItemBinding
import com.bumptech.glide.Glide

class MyBoxAdapter (var mContext : Context) : RecyclerView.Adapter<MyBoxAdapter.ViewHolder>() {

    var items = mutableListOf<KakaoImage>()


//    interface ItemClickListener {
//        fun onClick(item: KakaoImage,position: Int)
//    }
//
    interface ItemClickListener {
        fun onClick(item : KakaoImage, position: Int)
    }
    var clickListener : ItemClickListener? = null
//
//    fun setonItemClickListener (listener : ItemClickListener) {
//        this.clickListener = listener
//    }

    inner class ViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.ivItem
        val site = binding.tvImgSite
        val date = binding.tvDate
        val like = binding.ivFavorite
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clickListener?.onClick(items[position],position)
        }

        Glide.with(mContext)
            .load(items[position].url)
            .into(holder.img)
        holder.date.text = items[position].time.setTime()

        val type = if (items[position].type == Constants.SEARCH_TYPE_IMAGE) "[Video]" else "[Image]"
        holder.site.text = type + items[position].site
    }
}