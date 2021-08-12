package com.vonander.japancvcameraapp.di

import com.vonander.japancvcameraapp.network.util.SearchTagsDtoMapper
import com.vonander.japancvcameraapp.network.util.UploadResultDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideUploadResultDtoMapper(): UploadResultDtoMapper {
        return UploadResultDtoMapper()
    }

    @Singleton
    @Provides
    fun provideTagDtoMapper(): SearchTagsDtoMapper {
        return SearchTagsDtoMapper()
    }
}