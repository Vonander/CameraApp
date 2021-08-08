package com.vonander.japancvcameraapp.presentation.ui

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonander.japancvcameraapp.domain.Tag
import com.vonander.japancvcameraapp.interactors.SearchTags
import com.vonander.japancvcameraapp.interactors.TakePhoto
import com.vonander.japancvcameraapp.interactors.UploadPhoto
import com.vonander.japancvcameraapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    private val takePhoto: TakePhoto,
    private val uploadPhoto: UploadPhoto,
    private val searchTags: SearchTags,
    private @Named("Authorization") val authorization: String,
) : ViewModel() {

    val tags: MutableState<List<Tag>> = mutableStateOf(listOf())
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
                        event.uriString
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
        completion: (String) -> Unit
    ) {
        uploadPhoto.execute(
            uriString = uriString,
            completion = completion
        )
    }

    private fun searchTags(uriString: String) {
        searchTags.execute(
            authorization = authorization,
            uriString = uriString
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                tags.value = list
            }

            dataState.error?.let { error ->
                Log.e(TAG, "searchTags: $error")
            }

        }.launchIn(viewModelScope)
    }
}