package com.vonander.japancvcameraapp.interactors

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.vonander.japancvcameraapp.datastore.PhotoDataStore
import com.vonander.japancvcameraapp.presentation.MainActivity.Companion.getOutputDirectory
import com.vonander.japancvcameraapp.util.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TakePhoto(
    private val context: Context
) {
    fun execute(
        imageCapture: ImageCapture?,
        completion: () -> Unit
    ) {

        val imageCapture = imageCapture ?: return

        val photoFile = File(
            getOutputDirectory(context),
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.JAPAN
            ).format(System.currentTimeMillis()) + PHOTO_EXTENSION
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.d(TAG, "Photo capture succeeded: $savedUri")

                    savePhotoToDataStore(savedUri.toString(), completion)
                }
            }
        )
    }

    private fun savePhotoToDataStore(
        savedUri: String,
        completion: () -> Unit
    ) {
        val dataStore = PhotoDataStore()

        CoroutineScope(Dispatchers.Main).launch {
            dataStore.setPhotoUriString(
                context = context,
                newVaule = savedUri
            )

            completion()
        }
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
    }
}