package com.vonander.japancvcameraapp.interactors

import android.util.Log
import com.vonander.japancvcameraapp.domain.Tag
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.network.ImaggaService
import com.vonander.japancvcameraapp.network.util.TagDtoMapper
import com.vonander.japancvcameraapp.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTags(
    private val tagService: ImaggaService,
    private val dtoMapper: TagDtoMapper
) {

    fun execute(
        Authorization: String,
        uriString: String
    ): Flow<DataState<List<Tag>>> = flow {

        try {

            emit(DataState.loading())

            val tags = getTagsFromNetwork(
                Authorization,
                uriString
            )

            emit(DataState.success(tags))

        } catch (e: Exception) {
            Log.e(TAG, "error ${e.message}")
            emit(DataState.error<List<Tag>>(e.message ?: "Unknown Error"))
        }
    }

    private suspend fun getTagsFromNetwork(
        authorization: String,
        uriString: String
    ) :List<Tag> {
        return dtoMapper.toDomainList(
            tagService.search(
                authorization = authorization,
                image_url = uriString
            ).results
        )
    }
}