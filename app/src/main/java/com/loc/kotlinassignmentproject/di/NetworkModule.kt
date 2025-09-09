package com.loc.kotlinassignmentproject.di

import android.content.Context
import com.loc.kotlinassignmentproject.data.crypto.CryptoStore
import com.loc.kotlinassignmentproject.data.remote.ApiService
import com.loc.kotlinassignmentproject.data.remote.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCryptoStore(
        @ApplicationContext context: Context
    ): CryptoStore = CryptoStore(context)

    @Provides
    @Singleton
    fun provideOkHttp(cryptoStore: CryptoStore): OkHttpClient {
        val log = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(cryptoStore))
            .addInterceptor(log)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://httpbin.org/") // Replace with your real base URL
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
