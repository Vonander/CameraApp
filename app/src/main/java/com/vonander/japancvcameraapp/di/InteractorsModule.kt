package com.vonander.japancvcameraapp.di

import com.vonander.japancvcameraapp.BaseApplication
import com.vonander.japancvcameraapp.interactors.TakePhoto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun providesTakePhoto(
        app: BaseApplication
    ): TakePhoto {
        return TakePhoto(
            context = app
        )
    }
}