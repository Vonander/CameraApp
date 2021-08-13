package com.vonander.japancvcameraapp.interactors

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.presentation.MainActivity.Companion.getOutputDirectory
import com.vonander.japancvcameraapp.util.TAG
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TakePhoto(
    private val context: Context
) {
    fun execute(
        imageCapture: ImageCapture?,
        cameraPreviewCompletion: (DataState<String>) -> Unit,
        saveUriToViewModelCompletion: (DataState<String>) -> Unit
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
                    cameraPreviewCompletion(DataState.error("Photo capture failed: ${exc.message}"))
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)

                    showToast(message = "Photo capture succeeded")
                    cameraPreviewCompletion(DataState.success(savedUri.toString()))
                    saveUriToViewModelCompletion(DataState.success(savedUri.toString()))
                }
            }
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
    }
}