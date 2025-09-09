package com.loc.kotlinassignmentproject.data.repository

import com.loc.kotlinassignmentproject.data.remote.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun ping(): String = api.ping().string()
}
