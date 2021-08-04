package com.vonander.japancvcameraapp.interactors

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ContextUtils.getActivity
import com.vonander.japancvcameraapp.MainActivity.Companion.getOutputDirectory
import com.vonander.japancvcameraapp.util.TAG
import java.io.File

import java.text.SimpleDateFormat
import java.util.*

class TakePhoto(private val context: Context) {

    fun takePhoto(imageCapture: ImageCapture?) {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            getOutputDirectory(context),
            SimpleDateFormat(FILENAME_FORMAT, Locale.JAPAN
            ).format(System.currentTimeMillis()) + PHOTO_EXTENSION)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
    }
}