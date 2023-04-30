package com.example.testing.model

import com.example.core.domain.model.Event

object EventFactory {

    fun create(fakeEvent: FakeEvent) = when(fakeEvent) {
        FakeEvent.FakeEvent1 -> Event(
            id = 456,
            imageUrl = "http://fakeurl.jpg"
        )
    }

    sealed class FakeEvent {
        object FakeEvent1: FakeEvent()
    }
}