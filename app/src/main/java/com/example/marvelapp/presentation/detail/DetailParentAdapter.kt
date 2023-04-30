package com.example.marvelapp.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemChildDetailBinding
import com.example.marvelapp.databinding.ItemParentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader

class DetailParentAdapter(
    private val detailParentList: List<DetailParentVE>,
    private val imageLoader: ImageLoader
): RecyclerView.Adapter<DetailParentAdapter.DetailParentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailParentViewHolder {
        return DetailParentViewHolder.create(parent, imageLoader)
    }

    override fun getItemCount() = detailParentList.size


    override fun onBindViewHolder(holder: DetailParentViewHolder, position: Int) {
        holder.bind(detailParentList[position])
    }


    class DetailParentViewHolder(
        private val itemBinding: ItemParentDetailBinding,
        private val imageLoader: ImageLoader
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(detailParentVE: DetailParentVE) {
            itemBinding.textItemCategory.text = itemBinding.root.context.getString(detailParentVE.categoryStringResId)
            itemBinding.recyclerChildDetail.run {
                setHasFixedSize(true)
                adapter = DetailChildAdapter(detailParentVE.detailChildList, imageLoader)
            }
        }

        companion object {
            fun create(parent: ViewGroup, imageLoader: ImageLoader): DetailParentViewHolder {
                val itemBinding = ItemParentDetailBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return DetailParentViewHolder(itemBinding, imageLoader)
            }
        }

    }

}