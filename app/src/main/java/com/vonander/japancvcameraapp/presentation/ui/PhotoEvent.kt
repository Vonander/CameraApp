package com.vonander.japancvcameraapp.presentation.ui

import androidx.camera.core.ImageCapture
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.domain.model.Tags
import com.vonander.japancvcameraapp.domain.model.UploadResult

sealed class PhotoEvent {

    data class TakePhoto(
        val imageCapture: ImageCapture,
        val completion: () -> Unit
        ): PhotoEvent()

    data class UploadPhoto(
        val uriString: String,
        val completion: (DataState<UploadResult>) -> Unit
    ): PhotoEvent()

    data class SearchTags(
        val id: String?,
        val completion: (DataState<Tags>) -> Unit
        ): PhotoEvent()
}