package com.vonander.japancvcameraapp.network.model

import com.google.gson.annotations.SerializedName

data class UploadResultDto (

    @SerializedName("result")
    var result: HashMap<String, String>,

    @SerializedName("status")
    var status: HashMap<String, String>
)