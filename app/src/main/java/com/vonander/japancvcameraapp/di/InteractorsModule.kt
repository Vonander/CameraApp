package com.vonander.japancvcameraapp.di

import com.vonander.japancvcameraapp.BaseApplication
import com.vonander.japancvcameraapp.interactors.SearchTags
import com.vonander.japancvcameraapp.interactors.TakePhoto
import com.vonander.japancvcameraapp.interactors.UploadPhoto
import com.vonander.japancvcameraapp.network.util.SearchTagsDtoMapper
import com.vonander.japancvcameraapp.network.util.UploadResultDtoMapper
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

    @ViewModelScoped
    @Provides
    fun providesUploadPhoto(
        dtoMapper: UploadResultDtoMapper
    ): UploadPhoto {
        return UploadPhoto(
            dtoMapper = dtoMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideSearchTag(
        dtoMapper: SearchTagsDtoMapper
    ): SearchTags {
        return SearchTags(
            dtoMapper = dtoMapper
        )
    }
}