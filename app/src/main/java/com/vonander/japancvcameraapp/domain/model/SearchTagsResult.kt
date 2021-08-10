package com.vonander.japancvcameraapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class SearchTagsResult (
        var result: HashMap<String, List<HashMap<String, @RawValue Any>>>,
        var status: HashMap<String, String>
): Parcelable