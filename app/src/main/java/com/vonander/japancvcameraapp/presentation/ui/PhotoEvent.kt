package com.vonander.japancvcameraapp.presentation.ui

import androidx.camera.core.ImageCapture

sealed class PhotoEvent {
    object openCamera: PhotoEvent()

    data class TakePhoto(
        val imageCapture: ImageCapture,
        val completion: () -> Unit
        ): PhotoEvent()

    data class UploadPhoto(
        val uriString: String,
        val completion: (String) -> Unit
    ): PhotoEvent()

    data class SearchTags(
        val uriString: String
        ): PhotoEvent()
}