package com.loc.kotlinassignmentproject

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.loc.kotlinassignmentproject.data.api.AuthKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthKeyInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()

        client = OkHttpClient.Builder()
            .addInterceptor(AuthKeyInterceptor())
            .build()
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun authKeyHeader_isAdded() {
        // Enqueue a dummy response
        server.enqueue(MockResponse().setBody("{}"))

        val request = Request.Builder()
            .url(server.url("/test"))
            .build()

        val response = client.newCall(request).execute()
        response.close()

        val recordedRequest = server.takeRequest()
        val authHeader = recordedRequest.getHeader("AuthKey")

        assertNotNull("AuthKey header should be present", authHeader)
    }
}
