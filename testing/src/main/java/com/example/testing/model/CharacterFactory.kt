package com.example.testing.model

import com.example.core.domain.model.Character

class CharacterFactory {

    fun create(hero: Hero) = when(hero) {
        Hero.Spiderman -> Character(
            id = 1,
            name = "Spider-man",
            imageUrl = "https://i.annihil.us/u/prod/marvel/i/mg/b/80/4bc37bb4ae105/detail.jpg"
        )
        Hero.Wolverine -> Character(
            id = 2,
            name = "Wolverine",
            imageUrl = "https://marvelcdb.com/bundles/cards/32041.jpg"
        )
    }


    sealed class Hero {
        object Spiderman: Hero()
        object Wolverine: Hero()
    }
}