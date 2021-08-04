package com.vonander.japancvcameraapp.presentation.ui

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vonander.japancvcameraapp.R

class MainViewModel(

) : ViewModel() {

    val screenOrientation = mutableStateOf(1)
    val photoUriString = mutableStateOf("")
}