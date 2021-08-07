package com.vonander.japancvcameraapp.presentation.ui

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vonander.japancvcameraapp.interactors.TakePhoto
import com.vonander.japancvcameraapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val takePhoto: TakePhoto
) : ViewModel() {

    val screenOrientation = mutableStateOf(1)

    fun onTriggerEvent(event: PhotoEvent) {
        try {
            when(event) {
                is PhotoEvent.TakePhoto -> {
                    takePhoto(
                        event.imageCapture,
                        event.completion
                    )
                }
                is PhotoEvent.SendPhoto -> {

                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
        }
    }

    private fun takePhoto(
        imageCapture: ImageCapture,
        completion: (String) -> Unit
    ) {
        takePhoto.execute(imageCapture, completion)
    }
}