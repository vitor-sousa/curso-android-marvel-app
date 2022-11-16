package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.domain.model.Character
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemCharacterBinding

class CharacterViewHolder(
    private val itemCharacterBinding: ItemCharacterBinding
): RecyclerView.ViewHolder(itemCharacterBinding.root) {

    fun bind(character: Character) {
        itemCharacterBinding.apply {
            textName.text = character.name
            Glide.with(itemView)
                .load(character.imageUrl)
                .fallback(R.drawable.ic_img_loading_error)
                .into(imageCharacter)
        }
    }

    companion object {
        fun create(parent: ViewGroup): CharacterViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharacterBinding.inflate(inflater, parent, false)
            return CharacterViewHolder(itemBinding)
        }
    }
}