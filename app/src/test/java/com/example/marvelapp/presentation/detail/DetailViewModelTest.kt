package com.example.marvelapp.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.core.usecase.base.ResultStatus
import com.example.marvelapp.R
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.model.ComicFactory
import com.example.testing.model.EventFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var detailViewModel: DetailViewModel

    @Mock
    private lateinit var uiStateObserver: Observer<DetailViewModel.UiState>

    @Mock
    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    private val character = CharacterFactory().create(CharacterFactory.Hero.Spiderman)
    private val comics = listOf(ComicFactory.create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory.create(EventFactory.FakeEvent.FakeEvent1))

    private val categoriesPairResponse = ResultStatus.Success(comics to events)

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(getCharacterCategoriesUseCase)
        detailViewModel.uiState.observeForever(uiStateObserver)
    }

    @After
    fun tearDown() {
        mainCoroutineRule.testDispatcher.cancel()
    }



    @Test
    fun `should notify uiState with Success from UiState when get character categories return a success`() = runTest {

        whenever(
            getCharacterCategoriesUseCase.invoke(any())
        ).thenReturn(
            flowOf(
                categoriesPairResponse
            )
        )

        detailViewModel.getCharacterCategories(character.id)

        verify(uiStateObserver).onChanged(isA<DetailViewModel.UiState.Success>())

        val uiStateSuccess = detailViewModel.uiState.value as DetailViewModel.UiState.Success
        val categoriesParentList = uiStateSuccess.detailParentList

        assertEquals(2, categoriesParentList.size)
        assertEquals(R.string.details_comics_category, categoriesParentList[0].categoryStringResId)
        assertEquals(R.string.details_events_category, categoriesParentList[1].categoryStringResId)
    }

    @Test
    fun `should notify uiState with Empty from UiState when get character categories returns an empty result list`() = runTest {
        whenever(
            getCharacterCategoriesUseCase.invoke(any())
        ).thenReturn(
            flowOf(
                ResultStatus.Success(Pair(listOf(), listOf()))
            )
        )

        detailViewModel.getCharacterCategories(character.id)

        val resultExpect = DetailViewModel.UiState.Empty
        Assert.assertEquals(resultExpect, detailViewModel.uiState.value)
    }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns an only comics`() = runTest {


        whenever(
            getCharacterCategoriesUseCase.invoke(any())
        ).thenReturn(
            flowOf(
                ResultStatus.Success(comics to listOf<Event>())
            )
        )

        detailViewModel.getCharacterCategories(character.id)

        verify(uiStateObserver).onChanged(isA<DetailViewModel.UiState.Success>())

        val uiStateSuccess = detailViewModel.uiState.value as DetailViewModel.UiState.Success
        val categoriesParentList = uiStateSuccess.detailParentList

        assertEquals(1, categoriesParentList.size)
        assertEquals(R.string.details_comics_category, categoriesParentList[0].categoryStringResId)
    }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns an only events`() = runTest {


        whenever(
            getCharacterCategoriesUseCase.invoke(any())
        ).thenReturn(
            flowOf(
                ResultStatus.Success(listOf<Comic>() to events)
            )
        )

        detailViewModel.getCharacterCategories(character.id)

        verify(uiStateObserver).onChanged(isA<DetailViewModel.UiState.Success>())

        val uiStateSuccess = detailViewModel.uiState.value as DetailViewModel.UiState.Success
        val categoriesParentList = uiStateSuccess.detailParentList

        assertEquals(1, categoriesParentList.size)
        assertEquals(R.string.details_events_category, categoriesParentList[0].categoryStringResId)
    }

    @Test
    fun `should notify uiState with Error from UiState when getCharacterCategories returns an error`() = runTest {
        whenever(
            getCharacterCategoriesUseCase.invoke(any())
        ).thenReturn(
            flowOf(
                ResultStatus.Error(Throwable())
            )
        )

        detailViewModel.getCharacterCategories(character.id)

        val resultExpect = DetailViewModel.UiState.Error
        Assert.assertEquals(resultExpect, detailViewModel.uiState.value)
    }

}