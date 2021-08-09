package com.vonander.japancvcameraapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadResult (
    var result: HashMap<String, String>,
    var status: HashMap<String, String>
): Parcelable