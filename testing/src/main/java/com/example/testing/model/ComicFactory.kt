package com.example.testing.model

import com.example.core.domain.model.Comic

object ComicFactory {

    fun create(fakeComic: FakeComic) = when(fakeComic) {
        FakeComic.FakeComic1 -> Comic(
            id = 123,
            imageUrl = "http://fakecomigurl.jpg"
        )
    }

    sealed class FakeComic {
        object FakeComic1: FakeComic()
    }
}