package com.vonander.japancvcameraapp.interactors

import com.google.gson.Gson
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.domain.model.Tags
import com.vonander.japancvcameraapp.network.model.SearchTagsDto
import com.vonander.japancvcameraapp.network.util.SearchTagsDtoMapper
import com.vonander.japancvcameraapp.network.util.SearchTagsHandler
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SearchTags(
    private val dtoMapper: SearchTagsDtoMapper
) {

    fun executeTest(
        id: String?,
        completion: (DataState<Tags>) -> Unit
    ) = runBlocking {

        val id = id ?: return@runBlocking

        val handler = SearchTagsHandler()
        handler.setImageUploadId(id)

        val executor: ExecutorService = Executors.newCachedThreadPool()
        val future = executor.submit(handler)
        val responseString = future.get()

        val searchTagsDto: SearchTagsDto = Gson().fromJson(
            responseString,
            SearchTagsDto::class.java
        )

        // TODO Check status

        completion(DataState.success(getResultFromNetwork(searchTagsDto)))
    }

    private fun getResultFromNetwork(
        searchTagsDto: SearchTagsDto
    ): Tags {
        return dtoMapper.mapToDomainModel(searchTagsDto)
    }
}