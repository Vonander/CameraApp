package com.vonander.japancvcameraapp.presentation.ui.camera

sealed class CameraState(
    val facing: Int
) {
    object Front: CameraState(0)
    object Back: CameraState(1)
}
