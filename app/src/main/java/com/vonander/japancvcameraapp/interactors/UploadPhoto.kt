package com.vonander.japancvcameraapp.interactors

import android.net.Uri
import com.google.gson.Gson
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.domain.model.UploadResult
import com.vonander.japancvcameraapp.network.model.UploadResultDto
import com.vonander.japancvcameraapp.network.util.UploadPhotoHandler
import com.vonander.japancvcameraapp.network.util.UploadResultDtoMapper
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UploadPhoto(
    private val dtoMapper: UploadResultDtoMapper
) {
    fun execute(
        uriString: String,
        completion: (DataState<UploadResult>) -> Unit
    ) = runBlocking {

        val uriPath = Uri.parse(uriString).path ?: return@runBlocking

        val newFile = File(uriPath)
        val handler = UploadPhotoHandler()
        handler.setFileToUpload(newFile)

        val executor: ExecutorService = Executors.newCachedThreadPool()
        val future = executor.submit(handler)
        val responseString = future.get()

        val uploadResultDto: UploadResultDto = Gson().fromJson(
            responseString,
            UploadResultDto::class.java
        )

        completion(DataState.success(getResultFromNetwork(uploadResultDto)))
    }

    private fun getResultFromNetwork(
        uploadResultDto: UploadResultDto
    ): UploadResult {
        return dtoMapper.mapToDomainModel(
            uploadResultDto
        )
    }
}