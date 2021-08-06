package com.vonander.japancvcameraapp.presentation.ui

sealed class PhotoEvent {
    object openCamera: PhotoEvent()
    object TakePhoto: PhotoEvent()

    data class SendPhoto(val Uri: String): PhotoEvent()
}