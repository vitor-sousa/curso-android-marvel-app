package com.example.marvelapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.core.usecase.base.ResultStatus
import com.example.marvelapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState


    fun getComics(characterId: Int) = viewModelScope.launch {
        getCharacterCategoriesUseCase(GetCharacterCategoriesUseCase.GetComicParams(characterId)).watchStatus()
    }

    private fun Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>.watchStatus() = viewModelScope.launch {
        collect { status ->
            _uiState.postValue(
                when (status) {
                    ResultStatus.Loading -> UiState.Loading
                    is ResultStatus.Success -> {

                        val detailParentList = mutableListOf<DetailParentVE>()

                        val comics = status.data.first
                        val events = status.data.second

                        if (comics.isNotEmpty()) {
                            comics.map {
                                DetailChildVE(it.id, it.imageUrl)
                            }.also {
                                detailParentList.add(
                                    DetailParentVE(
                                        R.string.details_comics_category,
                                        it
                                    )
                                )
                            }
                        }

                        if (events.isNotEmpty()) {
                            events.map {
                                DetailChildVE(it.id, it.imageUrl)
                            }.also {
                                detailParentList.add(
                                    DetailParentVE(
                                        R.string.details_events_category,
                                        it
                                    )
                                )
                            }
                        }

                        UiState.Success(detailParentList)
                    }
                    is ResultStatus.Error -> UiState.Error
                }
            )
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentList: List<DetailParentVE>) : UiState()
        object Error : UiState()
    }

}
