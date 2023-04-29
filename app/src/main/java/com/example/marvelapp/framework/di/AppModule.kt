package com.example.marvelapp.framework.di

import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.framework.imageloader.ImageLoaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface AppModule {

    @Binds
    fun bindImageLoader(
        imageLoaderImpl: ImageLoaderImpl
    ) : ImageLoader

}