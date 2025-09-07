package com.loc.kotlinassignmentproject.data.api

import android.util.Base64
import com.loc.kotlinassignmentproject.crypto.CryptoProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val plainKey = "my-secret-key".toByteArray()
        val encrypted = CryptoProvider.engine.encryptForAuthKey(plainKey)
        val authKey = Base64.encodeToString(encrypted, Base64.NO_WRAP)

        val newReq = chain.request().newBuilder()
            .addHeader("AuthKey", authKey)
            .build()

        return chain.proceed(newReq)
    }
}
