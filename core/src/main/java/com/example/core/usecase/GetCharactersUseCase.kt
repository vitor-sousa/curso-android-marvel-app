package com.example.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.repository.CharacterRepository
import com.example.core.data.repository.StorageRepository
import com.example.core.domain.model.Character
import com.example.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

interface GetCharactersUseCase {
    operator fun invoke(params: GetCharactersParams): Flow<PagingData<Character>>

    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)
}

class GetCharactersUseCaseImpl @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val storageRepository: StorageRepository
) : PagingUseCase<GetCharactersUseCase.GetCharactersParams, Character>(), GetCharactersUseCase {

    override fun createFlowObservable(params: GetCharactersUseCase.GetCharactersParams): Flow<PagingData<Character>> {
        val orderBy = runBlocking { storageRepository.sorting.first() }
        return characterRepository.getCachedCharacters(params.query, orderBy, params.pagingConfig)
    }

}