package com.android.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.search.data.KakaoImage
import com.android.search.databinding.ListItemBinding
import com.bumptech.glide.Glide

class MyBoxAdapter (var myItemList : ArrayList<KakaoImage>) : RecyclerView.Adapter<MyBoxAdapter.ViewHolder>(){

    inner class ViewHolder(binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.ivItem
        val site = binding.tvImgSite
        val date = binding.tvDate
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itmeClick : ItemClick? = null

    override fun getItemCount(): Int {
        return myItemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itmeClick?.onClick(it,position)
        }

        Glide.with(holder.itemView.context)
            .load(myItemList[position].imageUrl)
            .into(holder.img)
        holder.site.text = myItemList[position].site
        holder.date.text = myItemList[position].time
            .substring(0,19)
            .replace("T"," ")
    }
}