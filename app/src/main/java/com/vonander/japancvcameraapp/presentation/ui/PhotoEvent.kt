package com.vonander.japancvcameraapp.presentation.ui

import androidx.camera.core.ImageCapture

sealed class PhotoEvent {
    object openCamera: PhotoEvent()

    data class TakePhoto(
        val imageCapture: ImageCapture,
        val completion: (String) -> Unit
        ): PhotoEvent()

    data class SendPhoto(
        val Uri: String
        ): PhotoEvent()
}