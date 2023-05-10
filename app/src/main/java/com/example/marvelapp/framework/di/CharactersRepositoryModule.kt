package com.example.marvelapp.framework.di

import com.example.core.data.repository.CharacterRepository
import com.example.core.data.repository.CharactersRemoteDataSource
import com.example.marvelapp.framework.CharactersRepositoryImpl
import com.example.marvelapp.framework.remote.RetrofitCharactersDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CharactersRepositoryModule {

    @Binds
    fun bindCharacterRepository(
        repository: CharactersRepositoryImpl
    ): CharacterRepository

    @Binds
    fun bindRemoteDataSource(
        dataSource: RetrofitCharactersDataSource
    ): CharactersRemoteDataSource
}