package com.vonander.japancvcameraapp.presentation.ui

import androidx.camera.core.ImageCapture
import com.vonander.japancvcameraapp.domain.data.DataState

sealed class PhotoEvent {

    data class TakePhoto(
        val imageCapture: ImageCapture,
        val completion: (DataState<String>) -> Unit
        ): PhotoEvent()

    data class UploadPhoto(
        val uriString: String,
        val completion: (Boolean) -> Unit
    ): PhotoEvent()

    object SearchTags: PhotoEvent()
}