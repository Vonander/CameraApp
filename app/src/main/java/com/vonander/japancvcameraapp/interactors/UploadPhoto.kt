package com.vonander.japancvcameraapp.interactors

import android.net.Uri
import com.google.gson.Gson
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.domain.model.UploadResult
import com.vonander.japancvcameraapp.network.model.UploadResultDto
import com.vonander.japancvcameraapp.network.util.UploadPhotoHandler
import com.vonander.japancvcameraapp.network.util.UploadResultDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UploadPhoto(
    private val dtoMapper: UploadResultDtoMapper
) {
    fun execute(
        uriString: String,
    ): Flow<DataState<UploadResult>> = flow {

        try {

            emit(DataState.loading())

            val uriPath = Uri.parse(uriString).path
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

            emit(DataState.success(getResultFromNetwork(uploadResultDto)))

        } catch (e: Exception) {
            emit(DataState.error<UploadResult>("Upload photo error $e"))
        }
    }

    private fun getResultFromNetwork(
        uploadResultDto: UploadResultDto
    ): UploadResult {
        return dtoMapper.mapToDomainModel(
            uploadResultDto
        )
    }
}