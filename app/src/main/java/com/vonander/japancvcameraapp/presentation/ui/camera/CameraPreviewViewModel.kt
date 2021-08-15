package com.vonander.japancvcameraapp.presentation.ui.camera

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonander.japancvcameraapp.BaseApplication
import com.vonander.japancvcameraapp.datastore.PhotoDataStore
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.interactors.TakePhoto
import com.vonander.japancvcameraapp.presentation.ui.PhotoEvent
import com.vonander.japancvcameraapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraPreviewViewModel @Inject constructor(
    private val app: BaseApplication,
    private val takePhoto: TakePhoto,
): ViewModel() {

    fun onTriggerEvent(event: PhotoEvent) {
        try {
            when (event) {
                is PhotoEvent.TakePhoto -> {
                    takePhoto(
                        imageCapture = event.imageCapture,
                        cameraPreviewCompletion = event.completion
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
        }
    }

    private fun takePhoto(
        imageCapture: ImageCapture,
        cameraPreviewCompletion: (DataState<String>) -> Unit
    ) {
        takePhoto.execute(
            imageCapture = imageCapture,
            cameraPreviewCompletion = cameraPreviewCompletion,
            saveUriToDataStoreCompletion = { dataState ->

                dataState.data?.let { uri ->
                    savePhotoToDataStore(savedUri = uri)
                }
            }
        )
    }

    private fun savePhotoToDataStore(
        savedUri: String
    ) {
        viewModelScope.launch {
            val dataStore = PhotoDataStore(app)
            dataStore.setPhotoUriString(
                newVaule = savedUri
            )
        }
    }
}