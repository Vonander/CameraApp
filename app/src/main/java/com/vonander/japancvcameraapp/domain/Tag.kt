package com.vonander.japancvcameraapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag (
        var confidence: Float,
        var tag: HashMap<String, String>
): Parcelable