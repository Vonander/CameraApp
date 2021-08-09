package com.vonander.japancvcameraapp.network.responses

import com.google.gson.annotations.SerializedName
import com.vonander.japancvcameraapp.network.model.TagDto

data class TagSearchResponse (

    @SerializedName("result")
    var results: List<TagDto>
)