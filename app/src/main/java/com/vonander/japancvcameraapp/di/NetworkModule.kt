package com.vonander.japancvcameraapp.di

import com.google.gson.GsonBuilder
import com.vonander.japancvcameraapp.network.ImaggaService
import com.vonander.japancvcameraapp.network.util.SearchTagsDtoMapper
import com.vonander.japancvcameraapp.network.util.UploadResultDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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

    @Singleton
    @Provides
    fun provideImaggaService(): ImaggaService {
        return Retrofit.Builder()
            .baseUrl("https://api.imagga.com/v2/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ImaggaService::class.java)
    }

    @Singleton
    @Provides
    @Named("Authorization")
    fun provideAuthorizationKey(): String {
        return "Basic YWNjX2RmZjg0NWRhZDc3NzY5NDozZDFkYmMwNDY1NWM5MTc2ZTZmZWNmNDQ3ODAyN2M0OA=="
    }
}