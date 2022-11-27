package com.example.marvelapp.presentation.characters

import androidx.paging.PagingData
import com.example.core.usecase.GetCharactersUseCase
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    private var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var getCharactersUseCase: GetCharactersUseCase

    private lateinit var charactersViewModel: CharactersViewModel

    private val characterFactory = CharacterFactory()

    private val pagingDataCharacters = PagingData.from(
        listOf(
            characterFactory.create(CharacterFactory.Hero.Spiderman),
            characterFactory.create(CharacterFactory.Hero.Wolverine)
        )
    )


    @Before
    fun setUp() {
        charactersViewModel = CharactersViewModel(getCharactersUseCase)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should validate the paging data object values when calling charactersPagingData`() = runBlockingTest {

        whenever(
            getCharactersUseCase.invoke(any())
        ).thenReturn(
            flowOf(
                pagingDataCharacters
            )
        )

        val result = charactersViewModel.charactersPagingData("")

        assertEquals(1, result.count())
    }


    @Test(expected = java.lang.RuntimeException::class)
    fun `should throwsan exception when the calling to the use case returns an exception`() = runBlockingTest {

        whenever(
            getCharactersUseCase.invoke(any())
        ).thenThrow(
            java.lang.RuntimeException()
        )

        charactersViewModel.charactersPagingData("")
    }

}