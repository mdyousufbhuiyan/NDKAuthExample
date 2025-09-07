package com.loc.kotlinassignmentproject.data.api

import com.loc.kotlinassignmentproject.data.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts/1")
    suspend fun getPost(): Post
}
