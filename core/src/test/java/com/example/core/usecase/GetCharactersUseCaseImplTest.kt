package com.example.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.repository.CharacterRepository
import com.example.core.data.repository.StorageRepository
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.pagingsource.PagingSourceFactory
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCharactersUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repository: CharacterRepository

    @Mock
    lateinit var storageRepository: StorageRepository

    private lateinit var getCharactersUseCase: GetCharactersUseCase

    private val hero = CharacterFactory().create(CharacterFactory.Hero.Spiderman)

    private val fakePagingData = PagingData.from(listOf(hero))

    @Before
    fun setUp() {
        getCharactersUseCase = GetCharactersUseCaseImpl(repository, storageRepository)
    }

    @Test
    fun `should validate flow paging data creation when invoke from use case is called`() =
        runTest {
            val pagingConfig = PagingConfig(20)
            val oderBy = "ascending"
            val query = "spider"

            whenever(repository.getCachedCharacters(query, oderBy, pagingConfig))
                .thenReturn(
                    flowOf(
                        fakePagingData
                    )
                )

            whenever(storageRepository.sorting)
                .thenReturn(
                    flowOf(
                        oderBy
                    )
                )


            val result = getCharactersUseCase
                .invoke(GetCharactersUseCase.GetCharactersParams(query, pagingConfig))

            verify(repository).getCachedCharacters(query,oderBy, pagingConfig)

            assertNotNull(result.first())
        }
}