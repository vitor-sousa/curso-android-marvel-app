package com.example.marvelapp.presentation.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.model.Character
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

            imageLoader.loadImage(imageCharacter, character.imageUrl)

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