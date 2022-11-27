package com.example.marvelapp.factory.response

import com.example.marvelapp.framework.network.response.CharacterResponse
import com.example.marvelapp.framework.network.response.DataContainerResponse
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.ThumbnailResponse

class DataWrapperResponseFactory {

    fun create() = DataWrapperResponse(
        copyright = "",
        data = DataContainerResponse(
            offset = 0,
            total = 2,
            results = listOf(
                CharacterResponse(
                    id = "1",
                    name = "Spider-man",
                    thumbnail = ThumbnailResponse(
                        path = "http://spider",
                        extension = "man"
                    )
                ),
                CharacterResponse(
                    id = "2",
                    name = "Wolverine",
                    thumbnail = ThumbnailResponse(
                        path = "http://wolverine",
                        extension = "com"
                    )
                ),
            )
        )
    )

}