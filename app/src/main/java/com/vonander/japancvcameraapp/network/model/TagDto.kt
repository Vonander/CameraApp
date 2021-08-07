package com.vonander.japancvcameraapp.network.model

import com.google.gson.annotations.SerializedName

data class TagDto(
    @SerializedName("confidence")
    var confidence: Float,

    @SerializedName("tag")
    var tag: HashMap<String, String>
)
