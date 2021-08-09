package com.vonander.japancvcameraapp.network.responses

import com.google.gson.annotations.SerializedName
import com.vonander.japancvcameraapp.network.model.UploadResultDto

data class UploadPhotoResult (

    @SerializedName("result")
    var result: UploadResultDto
)
