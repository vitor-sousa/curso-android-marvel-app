package com.example.marvelapp.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marvelapp.databinding.ItemCharacterBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.common.GenericViewHolder

class FavoritesViewHolder(
    private val itemBinding: ItemCharacterBinding,
    private val imageLoader: ImageLoader
): GenericViewHolder<FavoriteItem>(itemBinding) {

    override fun bind(data: FavoriteItem) {
        itemBinding.textName.text = data.name
        imageLoader.loadImage(itemBinding.imageCharacter, data.imageUrl)
    }

    companion object {
        fun create(parent: ViewGroup, imageLoader: ImageLoader): FavoritesViewHolder {
            val itemBinding = ItemCharacterBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return FavoritesViewHolder(itemBinding, imageLoader)
        }
    }

}