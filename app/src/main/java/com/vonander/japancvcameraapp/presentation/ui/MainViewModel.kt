package com.vonander.japancvcameraapp.presentation.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel(

) : ViewModel() {

    val screenOrientation = mutableStateOf(1)
    val photoUriString = mutableStateOf("")
}