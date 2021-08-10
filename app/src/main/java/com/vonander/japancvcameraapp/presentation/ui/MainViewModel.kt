package com.vonander.japancvcameraapp.presentation.ui

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.domain.model.Tags
import com.vonander.japancvcameraapp.domain.model.UploadResult
import com.vonander.japancvcameraapp.interactors.SearchTags
import com.vonander.japancvcameraapp.interactors.TakePhoto
import com.vonander.japancvcameraapp.interactors.UploadPhoto
import com.vonander.japancvcameraapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    private val takePhoto: TakePhoto,
    private val uploadPhoto: UploadPhoto,
    private val searchTags: SearchTags,
    private @Named("Authorization") val authorization: String,
) : ViewModel() {

    val tags: MutableState<List<Tags>> = mutableStateOf(listOf())
    val screenOrientation = mutableStateOf(1)
    val loading = mutableStateOf(false)

    fun onTriggerEvent(event: PhotoEvent) {
        try {
            when(event) {
                is PhotoEvent.TakePhoto -> {
                    takePhoto(
                        event.imageCapture,
                        event.completion
                    )
                }
                is PhotoEvent.UploadPhoto -> {
                    uploadPhoto(
                        event.uriString,
                        event.completion
                    )
                }
                is PhotoEvent.SearchTags -> {
                    searchTags(
                        event.id,
                        event.completion
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
        }
    }

    private fun takePhoto(
        imageCapture: ImageCapture,
        completion: () -> Unit
    ) {
        takePhoto.execute(imageCapture, completion)
    }

    private fun uploadPhoto(
        uriString: String,
        completion: (DataState<UploadResult>) -> Unit
    ) {
        uploadPhoto.execute(
            uriString = uriString,
            completion = completion
        )
    }

    private fun searchTags(
        id: String?,
        completion: (DataState<Tags>) -> Unit
    ) {
        searchTags.executeTest(
            id = id,
            completion = completion
        )
    }
}