package com.example.core.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.repository.CharacterRepository
import com.example.core.usecase.base.PagingUseCase
import com.example.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(
    private val characterRepository: CharacterRepository
): PagingUseCase<GetCharactersUseCase.GetCharactersParams, Character>() {

    override fun createFlowObservable(params: GetCharactersParams): Flow<PagingData<Character>> {
        return Pager(config = params.pagingConfig) {
            characterRepository.getCharacters(params.query)
        }.flow
    }


    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)
}