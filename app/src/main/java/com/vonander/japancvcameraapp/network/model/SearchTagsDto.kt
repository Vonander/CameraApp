package com.vonander.japancvcameraapp.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue

data class SearchTagsDto(

    @SerializedName("result")
    var result: HashMap<String, List<HashMap<String, @RawValue Any>>>,

    @SerializedName("status")
    var status: HashMap<String, String>
)
