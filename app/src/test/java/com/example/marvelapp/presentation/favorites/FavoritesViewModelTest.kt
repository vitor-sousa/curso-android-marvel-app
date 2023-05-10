package com.example.marvelapp.presentation.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.domain.model.Character
import com.example.core.usecase.GetFavoritesUseCase
import com.example.core.usecase.base.ResultStatus
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class FavoritesViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FavoritesViewModel

    @Mock
    private lateinit var getFavoritesUseCase: GetFavoritesUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<FavoritesViewModel.UiState>

    private val characterList = listOf(CharacterFactory().create(CharacterFactory.Hero.Spiderman), CharacterFactory().create(CharacterFactory.Hero.Wolverine))


    @Before
    fun setUp() {
        viewModel = FavoritesViewModel(getFavoritesUseCase, mainCoroutineRule.testDispatcherProvider)
        viewModel.state.observeForever(uiStateObserver)
    }

    @After
    fun tearDown() {
        mainCoroutineRule.testDispatcher.cancel()
    }

    @Test
    fun `should return a list of FavoriteItem when getAll is called`() = runTest {
        whenever(
            getFavoritesUseCase.invoke(any())
        ).thenReturn(
            flowOf(
                characterList
            )
        )

        viewModel.getAll()

        verify(uiStateObserver).onChanged(isA<FavoritesViewModel.UiState.ShowFavorites>())

    }

    @Test
    fun `should return a empty uiState when getAll is called and returna empty list`() = runTest {
        whenever(
            getFavoritesUseCase.invoke(any())
        ).thenReturn(
            flowOf(
                emptyList()
            )
        )

        viewModel.getAll()

        verify(uiStateObserver).onChanged(FavoritesViewModel.UiState.ShowEmpty)
    }
}