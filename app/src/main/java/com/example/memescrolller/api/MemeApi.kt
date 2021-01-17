package com.example.memescrolller.api

import retrofit2.http.*

interface MemeApi {

    companion object {
        const val BASE_URL = "https://www.reddit.com/r/"
        var alwaysShowNewMemes: Boolean = true
    }

    @GET("memes.json")
    suspend fun getMeme(@Query("after") after: String): MemeResponse
}