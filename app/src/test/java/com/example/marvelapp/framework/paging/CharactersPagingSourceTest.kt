package com.example.marvelapp.framework.paging

import android.accounts.NetworkErrorException
import androidx.paging.PagingSource
import com.example.core.data.repository.CharactersRemoteDataSource
import com.example.marvelapp.factory.response.DataWrapperResponseFactory
import com.example.marvelapp.framework.network.response.DataContainerResponse
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharactersPagingSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var remoteDataSource: CharactersRemoteDataSource<DataWrapperResponse>

    private lateinit var charactersPagingSource: CharactersPagingSource
    private val characterFactory = CharacterFactory()

    private val dataWrapperResponse = DataWrapperResponseFactory().create()

    @Before
    fun setUp() {
        charactersPagingSource = CharactersPagingSource(remoteDataSource, "")
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should return success load result when load is called `() = runBlockingTest {

        //Arrange
        whenever(
            remoteDataSource.fetchCharacters(any())
        ).thenReturn(
            dataWrapperResponse
        )


        //Act
        val result = charactersPagingSource.load(
            PagingSource.LoadParams.Refresh(null,2,false)
        )


        //Assert
        val expected = listOf(
            characterFactory.create(CharacterFactory.Hero.Spiderman),
            characterFactory.create(CharacterFactory.Hero.Wolverine)
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = expected,
                prevKey = null,
                nextKey = 20
            ),
            result
        )
    }


    @Test
    fun `should return a error load result when load is called`() = runBlockingTest {

        //Arrange
        val exception = RuntimeException()
        whenever(
            remoteDataSource.fetchCharacters(any())
        ).thenThrow(
            exception
        )


        //Act
        val result = charactersPagingSource.load(
            PagingSource.LoadParams.Refresh(null,2,false)
        )

        assertEquals(
            PagingSource.LoadResult.Error<Int, com.example.core.domain.model.Character>(
                exception
            ),
            result
        )

    }


}