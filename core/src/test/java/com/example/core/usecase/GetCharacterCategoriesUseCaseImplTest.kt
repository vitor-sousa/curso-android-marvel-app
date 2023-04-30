package com.example.core.usecase

import com.example.core.data.repository.CharacterRepository
import com.example.core.usecase.base.ResultStatus
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.model.ComicFactory
import com.example.testing.model.EventFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCharacterCategoriesUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var getCharacterCategoriesUseCaseImpl: GetCharacterCategoriesUseCaseImpl

    @Mock
    private lateinit var repository : CharacterRepository


    private val character = CharacterFactory().create(CharacterFactory.Hero.Spiderman)
    private val comics = listOf(ComicFactory.create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory.create(EventFactory.FakeEvent.FakeEvent1))

    @Before
    fun setUp() {
        getCharacterCategoriesUseCaseImpl =
            GetCharacterCategoriesUseCaseImpl(repository, mainCoroutineRule.testDispatcherProvider)

    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should return Success from ResultStatus when get both requests return success`() = runTest {
        whenever(repository.getComics(any())).thenReturn(comics)
        whenever(repository.getEvents(any())).thenReturn(events)

        val result = getCharacterCategoriesUseCaseImpl.invoke(
            GetCharacterCategoriesUseCase.GetCategoriesParams(character.id)
        )

        val resultList = result.toList()

        assertEquals(2, resultList.size)
        assertEquals(ResultStatus.Loading, resultList[0])
        assertTrue(resultList[1] is ResultStatus.Success)
    }

    @Test
    fun `should return Error from ResultStatus when get events request returns error`() = runTest {
        whenever(repository.getComics(any())).thenReturn(comics)
        whenever(repository.getEvents(any())).thenAnswer { throw Throwable() }

        val result = getCharacterCategoriesUseCaseImpl.invoke(
            GetCharacterCategoriesUseCase.GetCategoriesParams(character.id)
        )

        val resultList = result.toList()

        assertEquals(ResultStatus.Loading, resultList[0])
        assertTrue(resultList[1] is ResultStatus.Error)
    }

    @Test
    fun `should return Error from ResultStatus when get comics request returns error`() = runTest {
        whenever(repository.getComics(any())).thenAnswer { throw Throwable() }

        val result = getCharacterCategoriesUseCaseImpl.invoke(
            GetCharacterCategoriesUseCase.GetCategoriesParams(character.id)
        )

        val resultList = result.toList()

        assertEquals(2, resultList.size)
        assertEquals(ResultStatus.Loading, resultList[0])
        assertTrue(resultList[1] is ResultStatus.Error)
    }
}