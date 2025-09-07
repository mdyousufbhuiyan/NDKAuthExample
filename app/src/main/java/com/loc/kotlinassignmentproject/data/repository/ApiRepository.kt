package com.loc.kotlinassignmentproject.data.repository

import com.loc.kotlinassignmentproject.data.api.ApiService
import com.loc.kotlinassignmentproject.data.api.RetrofitProvider
import com.loc.kotlinassignmentproject.data.model.Post

class MainRepository(val api: ApiService) {

    suspend fun fetchPost(): Post = api.getPost() // <-- Explicit return type
}