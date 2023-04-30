package com.example.marvelapp.factory.response

import com.example.core.domain.model.Character
import com.example.core.domain.model.CharacterPaging
import com.example.marvelapp.framework.network.response.CharacterResponse
import com.example.marvelapp.framework.network.response.DataContainerResponse
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.ThumbnailResponse

class CharacterPagingFactory {

    fun create() = CharacterPaging(
        offset = 0,
        total = 2,
        characters = listOf(
            Character(
                id = 1,
                name = "Spider-man",
                imageUrl = "https://i.annihil.us/u/prod/marvel/i/mg/b/80/4bc37bb4ae105/detail.jpg"
            ),
            Character(
                id = 2,
                name = "Wolverine",
                imageUrl = "https://marvelcdb.com/bundles/cards/32041.jpg"
            ),
        )
    )

}