package com.example.testing.model

import com.example.core.domain.model.Character

class CharacterFactory {

    fun create(hero: Hero) = when(hero) {
        Hero.Spiderman -> Character(
            "Spider-man",
            "https://spider.man"
        )
        Hero.Wolverine -> Character(
            "Wolverine",
            "https://wolverine.com"
        )
    }


    sealed class Hero {
        object Spiderman: Hero()
        object Wolverine: Hero()
    }
}