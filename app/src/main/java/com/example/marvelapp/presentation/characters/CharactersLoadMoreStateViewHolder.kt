package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharacterLoadMoreStateBinding

class CharactersLoadMoreStateViewHolder(
    itemBinding: ItemCharacterLoadMoreStateBinding,
    private val retry: () -> Unit
): RecyclerView.ViewHolder(itemBinding.root) {

    private val binding = ItemCharacterLoadMoreStateBinding.bind(itemView)

    fun bind(loadState: LoadState) {
        binding.progressLoadingMore.isVisible = loadState is LoadState.Loading
        binding.textTryAgain.isVisible = loadState is LoadState.Error
        binding.textTryAgain.setOnClickListener { retry() }
    }


    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): CharactersLoadMoreStateViewHolder {
            val itemBinding = ItemCharacterLoadMoreStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return CharactersLoadMoreStateViewHolder(itemBinding, retry)
        }
    }
}