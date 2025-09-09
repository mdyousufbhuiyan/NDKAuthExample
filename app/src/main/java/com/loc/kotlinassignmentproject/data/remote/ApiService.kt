package com.loc.kotlinassignmentproject.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET

interface ApiService {
    // Example endpoint. Replace by your API endpoints.
    @GET("get")
    suspend fun ping(): ResponseBody
}
