package com.vonander.japancvcameraapp.network

import com.vonander.japancvcameraapp.network.responses.TagSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ImaggaService {
    @GET("uploads")
    suspend fun search(
        @Header("Authorization") authorization: String,
        @Query("image_url") image_url: String
    ): TagSearchResponse
}