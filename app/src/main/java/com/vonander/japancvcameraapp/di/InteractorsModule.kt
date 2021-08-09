package com.vonander.japancvcameraapp.di

import com.vonander.japancvcameraapp.BaseApplication
import com.vonander.japancvcameraapp.interactors.SearchTags
import com.vonander.japancvcameraapp.interactors.TakePhoto
import com.vonander.japancvcameraapp.interactors.UploadPhoto
import com.vonander.japancvcameraapp.network.ImaggaService
import com.vonander.japancvcameraapp.network.util.TagDtoMapper
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
    fun providesUploadPhoto(): UploadPhoto {
        return UploadPhoto()
    }

    @ViewModelScoped
    @Provides
    fun provideSearchTag(
        tagService: ImaggaService,
        dtoMapper: TagDtoMapper
    ): SearchTags {
        return SearchTags(
            tagService = tagService,
            dtoMapper = dtoMapper
        )
    }
}