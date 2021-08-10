package com.vonander.japancvcameraapp.network.responses

import com.google.gson.annotations.SerializedName
import com.vonander.japancvcameraapp.network.model.SearchTagsDto

data class TagSearchResponse (

    @SerializedName("result")
    var results: List<SearchTagsDto>
)
