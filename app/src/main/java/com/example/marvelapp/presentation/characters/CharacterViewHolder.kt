package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.domain.model.Character
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemCharacterBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.util.OnCharacterItemClick

class CharacterViewHolder(
    private val imageLoader: ImageLoader,
    private val itemCharacterBinding: ItemCharacterBinding,
    private val onItemClick: OnCharacterItemClick
): RecyclerView.ViewHolder(itemCharacterBinding.root) {

    fun bind(character: Character) {
        itemCharacterBinding.apply {
            textName.text = character.name
            imageCharacter.transitionName = character.name

            imageLoader.loadImage(imageCharacter, character.imageUrl, R.drawable.ic_img_loading_error)

            itemView.setOnClickListener {
                onItemClick.invoke(character, imageCharacter)
            }
        }
    }

    companion object {
        fun create(
            imageLoader: ImageLoader,
            parent: ViewGroup,
            onItemClick: OnCharacterItemClick
        ): CharacterViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharacterBinding.inflate(inflater, parent, false)
            return CharacterViewHolder(imageLoader, itemBinding, onItemClick)
        }
    }
}