package com.loc.kotlinassignmentproject

import com.loc.kotlinassignmentproject.data.api.AuthKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertNotNull
import org.junit.Test

class AuthKeyInterceptorTest {

    @Test
    fun headerIsPresent() {
        val server = MockWebServer().apply {
            enqueue(MockResponse().setBody("{}").setResponseCode(200))
            start()
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthKeyInterceptor())
            .build()

        val request = Request.Builder()
            .url(server.url("/get"))
            .build()

        client.newCall(request).execute().use {
            val recorded = server.takeRequest()
            val header = recorded.getHeader("AuthKey")
            assertNotNull(header)
        }

        server.shutdown()
    }
}
