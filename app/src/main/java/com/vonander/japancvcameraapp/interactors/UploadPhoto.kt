package com.vonander.japancvcameraapp.interactors

import android.net.Uri
import com.vonander.japancvcameraapp.network.util.UploadPhotoHandler
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UploadPhoto {
    fun execute(
        uriString: String,
        completion: (String) -> Unit
    ) = runBlocking {

        val uriPath = Uri.parse(uriString).path ?: return@runBlocking

        val newFile = File(uriPath)
        val handler = UploadPhotoHandler()
        handler.setFileToUpload(newFile)

        val executor: ExecutorService = Executors.newCachedThreadPool()
        val future = executor.submit(handler)

        completion(future.get())
    }
}