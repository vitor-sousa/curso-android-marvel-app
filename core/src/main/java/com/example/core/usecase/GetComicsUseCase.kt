package com.example.core.usecase

import com.example.core.data.repository.CharacterRepository
import com.example.core.domain.model.Comic
import com.example.core.usecase.base.ResultStatus
import com.example.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetComicsUseCase {
    operator fun invoke(params: GetComicParams): Flow<ResultStatus<List<Comic>>>
    data class GetComicParams(val characterId: Int)
}

class GetComicsUseCaseImpl @Inject constructor(
    private val repository: CharacterRepository
): GetComicsUseCase, UseCase<GetComicsUseCase.GetComicParams, List<Comic>>() {

    override suspend fun doWork(params: GetComicsUseCase.GetComicParams): ResultStatus<List<Comic>> {
        val comics = repository.getComics(params.characterId)
        return ResultStatus.Success(comics)
    }

}