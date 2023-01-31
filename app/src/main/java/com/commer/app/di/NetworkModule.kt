package com.commer.app.di

import com.commer.app.data.remote.ApiClient
import com.commer.app.data.remote.ApiService
import com.commer.app.data.remote.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(
        okHttpClient: OkHttpClient
    ): ApiService {
        return ApiClient(okHttpClient).instance()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient = okHttpClient()

}