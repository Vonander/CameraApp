package com.vonander.japancvcameraapp.interactors

import android.content.Context
import android.net.Uri
import com.vonander.japancvcameraapp.network.util.ImaggaImageUpload
import kotlinx.coroutines.runBlocking
import java.io.File

class UploadPhoto(
    private val context: Context
) {

    fun execute(
        uriString: String,
        completion: (String) -> Unit
    ) = runBlocking {

        val uriPath = Uri.parse(uriString).path ?: return@runBlocking

        val newFile = File(uriPath)

        ImaggaImageUpload.execute(newFile)
    }
}