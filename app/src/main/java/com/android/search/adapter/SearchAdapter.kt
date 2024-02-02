package com.android.search.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.android.search.MainActivity
import com.android.search.data.Constants
import com.android.search.data.KakaoImage
import com.android.search.databinding.ListItemBinding
import com.android.search.retrofit.NetWorkClient
import com.android.search.viewmodel.SearchViewModel
import com.bumptech.glide.Glide
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class SearchAdapter (val context: Context) : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    var items = mutableListOf<KakaoImage>()
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
            .load(items[position].url)
            .into(holder.img)
        holder.date.text = items[position].time.setTime()

        val type = if (items[position].type == Constants.SEARCH_TYPE_IMAGE) "[Video]" else "[Image]"
        holder.site.text = type + items[position].site
        holder.like.visibility = if (items[position].isLike) View.VISIBLE else View.INVISIBLE
    }

    private fun String.setTime() : String {
        val receiveTime = OffsetDateTime.parse(this)
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(receiveTime)
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