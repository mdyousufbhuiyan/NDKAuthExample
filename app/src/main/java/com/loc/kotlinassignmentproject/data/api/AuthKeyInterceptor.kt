package com.loc.kotlinassignmentproject.data.api

import android.util.Base64
import com.loc.kotlinassignmentproject.crypto.CryptoProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val ts = System.currentTimeMillis()
        val method = req.method.uppercase()
        val path = req.url.encodedPath

        val payload = "$ts:$method:$path".toByteArray(Charsets.UTF_8)
        val encrypted = CryptoProvider.engine.encryptForAuthKey(payload)
        val authKey = Base64.encodeToString(encrypted, Base64.NO_WRAP)

        val newReq = chain.request().newBuilder()
            .addHeader("AuthKey", authKey)
            .build()

        return chain.proceed(newReq)
    }
}
