package com.vonander.japancvcameraapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
    var confidence: String,
    var tag: String
): Parcelable
