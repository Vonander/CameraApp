package com.vonander.japancvcameraapp.interactors

import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.domain.model.UploadResult
import com.vonander.japancvcameraapp.network.model.UploadResultDto
import com.vonander.japancvcameraapp.network.util.UploadPhotoHandler
import com.vonander.japancvcameraapp.network.util.UploadResultDtoMapper
import com.vonander.japancvcameraapp.util.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
            Log.e(TAG, "Upload photo error $e", e.cause)
            emit(DataState.error<UploadResult>("Upload photo error $e"))
        }

    }.flowOn(Dispatchers.IO)

    private fun getResultFromNetwork(
        uploadResultDto: UploadResultDto
    ): UploadResult {
        return dtoMapper.mapToDomainModel(
            uploadResultDto
        )
    }
}