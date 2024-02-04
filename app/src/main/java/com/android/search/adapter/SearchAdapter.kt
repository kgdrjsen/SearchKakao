package com.android.search.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.search.data.Constants
import com.android.search.data.KakaoImage
import com.android.search.data.Utils.setTime
import com.android.search.databinding.ListItemBinding
import com.bumptech.glide.Glide

const val Tag = "SearchAdapter"

class SearchAdapter (val context: Context) : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    var items = mutableListOf<KakaoImage>()
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it,position)
            Log.d(Tag,"#aaa click? = $position")
        }
        viewBind(holder, position)
    }

    private fun viewBind(holder: ItemViewHolder, position: Int) {
        //섬네일 url받아오기
        Glide.with(holder.itemView.context)
            .load(items[position].url)
            .into(holder.img)
        holder.date.text = items[position].time.setTime()

        val type = if (items[position].type == Constants.SEARCH_TYPE_IMAGE) "[Video]" else "[Image]"
        holder.site.text = type + items[position].site
        holder.like.visibility = if (items[position].isLike) View.VISIBLE else View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return items.size
    }
    
    inner class ItemViewHolder (binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.ivItem
        val site = binding.tvImgSite
        val date = binding.tvDate
        val like = binding.ivFavorite
    }
}