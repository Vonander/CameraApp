package com.vonander.japancvcameraapp.interactors

import com.google.gson.Gson
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.domain.model.SearchTagsResult
import com.vonander.japancvcameraapp.network.model.SearchTagsDto
import com.vonander.japancvcameraapp.network.util.SearchTagsDtoMapper
import com.vonander.japancvcameraapp.network.util.SearchTagsHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SearchTags(
    private val dtoMapper: SearchTagsDtoMapper
) {
    fun execute(
        id: String?,
    ): Flow<DataState<SearchTagsResult>> = flow {

        try {

            emit(DataState.loading())

            val handler = SearchTagsHandler()
            handler.setImageUploadId(id)

            val executor: ExecutorService = Executors.newCachedThreadPool()
            val future = executor.submit(handler)
            val responseString = future.get()

            val tagDto: SearchTagsDto = Gson().fromJson(
                responseString,
                SearchTagsDto::class.java
            )

            emit(DataState.success(getResultFromNetwork(tagDto)))

        } catch (e: Exception) {
            emit(DataState.error<SearchTagsResult>("Search tags error $e"))
        }
    }.flowOn(Dispatchers.IO)

    private fun getResultFromNetwork(
        tagDto: SearchTagsDto
    ): SearchTagsResult {
        return dtoMapper.mapToDomainModel(tagDto)
    }
}