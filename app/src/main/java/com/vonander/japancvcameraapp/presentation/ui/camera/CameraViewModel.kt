package com.vonander.japancvcameraapp.presentation.ui.camera

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CameraViewModel(

) : ViewModel() {

    val screenOrientation = mutableStateOf(1)

}