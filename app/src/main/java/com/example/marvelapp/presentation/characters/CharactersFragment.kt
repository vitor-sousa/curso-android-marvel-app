package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.characters.adapters.CharacterAdapter
import com.example.marvelapp.presentation.characters.adapters.CharactersLoadMoreStateAdapter
import com.example.marvelapp.presentation.characters.adapters.CharactersRefreshStateAdapter
import com.example.marvelapp.presentation.detail.DetailViewArg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment: Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    private val headerAdapter: CharactersRefreshStateAdapter by lazy {
        CharactersRefreshStateAdapter(
            characterAdapter::retry
        )
    }

    private val footerAdapter: CharactersLoadMoreStateAdapter by lazy {
        CharactersLoadMoreStateAdapter(
            characterAdapter::retry
        )
    }

    private val characterAdapter: CharacterAdapter by lazy {
        CharacterAdapter(imageLoader) { character, view ->
            val extras = FragmentNavigatorExtras(
                view to character.name
            )
            val directions = CharactersFragmentDirections.actionCharactersFragmentToDetailFragment(
                character.name,
                DetailViewArg(character.id, character.name, character.imageUrl)
            )

            findNavController().navigate(directions, extras)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCharactersBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCharactersAdapter()
        observeInitialLoadState()

        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is CharactersViewModel.UiState.SearchResult -> {
                    characterAdapter.submitData(viewLifecycleOwner.lifecycle, uiState.data)
                }
            }
        }
        viewModel.searchCharacter()
    }

    private fun initCharactersAdapter() {
        postponeEnterTransition()
        binding.recyclerCharacters.run {
            setHasFixedSize(true)
            adapter = characterAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )
            viewTreeObserver.addOnDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                characterAdapter.loadStateFlow.collectLatest { loadState ->

                    headerAdapter.loadState = loadState.mediator
                        ?.refresh
                        ?.takeIf {
                            it is LoadState.Error && characterAdapter.itemCount > 0
                        } ?: loadState.prepend

                    binding.flipperCharacters.displayedChild = when {

                        loadState.mediator?.refresh is LoadState.Loading -> {
                            setShimmerVisibility(true)
                            FLIPPER_CHILD_LOADING
                        }

                        loadState.mediator?.refresh is LoadState.Error
                                && characterAdapter.itemCount == 0 -> {
                            setShimmerVisibility(false)
                            binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
                                characterAdapter.retry()
                            }
                            FLIPPER_CHILD_ERROR
                        }

                        loadState.source.refresh is LoadState.NotLoading
                                || loadState.mediator?.refresh is LoadState.NotLoading -> {
                            setShimmerVisibility(false)
                            FLIPPER_CHILD_SUCCESS
                        }

                        else -> {
                            setShimmerVisibility(false)
                            FLIPPER_CHILD_SUCCESS
                        }

                    }
                }
            }
        }
    }


    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewCharactersLoadingState.shimmerCharacters.run {
            isVisible = visibility
            if (visibility) {
                startShimmer()
            } else stopShimmer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_SUCCESS = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }

}