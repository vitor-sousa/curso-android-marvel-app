package com.example.marvelapp.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.extensions.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailViewArg = args.detailViewArg

        binding.imageCharacter.run {
            transitionName = detailViewArg.name
            imageLoader.loadImage(this, detailViewArg.imageUrl)
        }

        setSharedElementTransitionOnEnter()

        loadCategoriesAndObserveUiState(detailViewArg)
        setAndObserverFavoriteUiState(detailViewArg)

    }

    private fun loadCategoriesAndObserveUiState(detailViewArg: DetailViewArg) {

        viewModel.categories.load(detailViewArg.characterId)

        viewModel.categories.state.observe(viewLifecycleOwner) { uiState ->
            binding.flipperDetail.displayedChild = when (uiState) {
                UiActionStateLiveData.UiState.Loading -> {
                    setShimmerVisibility(true)
                    FLIPPER_CHILD_LOADING
                }
                is UiActionStateLiveData.UiState.Success -> {
                    binding.recyclerParentDetail.run {
                        setHasFixedSize(true)
                        adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
                    }
                    setShimmerVisibility(false)
                    FLIPPER_CHILD_DETAIL
                }
                is UiActionStateLiveData.UiState.Error -> {
                    binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
                        viewModel.categories.load(detailViewArg.characterId)
                    }
                    setShimmerVisibility(false)
                    FLIPPER_CHILD_ERROR
                }
                is UiActionStateLiveData.UiState.Empty -> {
                    setShimmerVisibility(false)
                    FLIPPER_CHILD_EMPTY
                }
            }
        }
    }

    private fun setAndObserverFavoriteUiState(detailViewArg: DetailViewArg) {

        binding.imageFavoriteIcon.setOnClickListener {
            viewModel.favorite.update(detailViewArg)
        }

        viewModel.favorite.state.observe(viewLifecycleOwner) { favoriteUiState ->
            binding.flipperFavorite.displayedChild = when(favoriteUiState) {
                is FavoriteUiActionStateLiveData.UiState.Loading -> FLIPPER_FAVORITE_LOADING
                is FavoriteUiActionStateLiveData.UiState.Icon -> {
                    binding.imageFavoriteIcon.setImageResource(favoriteUiState.icon)
                    FLIPPER_FAVORITE_ICON
                }
                is FavoriteUiActionStateLiveData.UiState.Error -> {
                    showShortToast(favoriteUiState.messageResId)
                    FLIPPER_FAVORITE_ICON
                }
            }
        }
    }


    // Define a animação da transição como "move"
    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewDetailLoadingState.shimmerCharacters.run {
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
        private const val FLIPPER_CHILD_DETAIL = 1
        private const val FLIPPER_CHILD_ERROR = 2
        private const val FLIPPER_CHILD_EMPTY = 3

        private const val FLIPPER_FAVORITE_ICON = 0
        private const val FLIPPER_FAVORITE_LOADING = 1
    }

}