package com.commer.app.data.remote

import com.commer.app.data.local.CommerSharedPref
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            var token = CommerSharedPref.userToken
            CommerSharedPref.userToken?.let {
                val apiService = ApiClient(okHttpClient()).instance()
                apiService.postRefreshToken(
                    "refresh_token",
                    "my-client-web",
                    "password",
                    CommerSharedPref.userRefreshToken!!
                ).suspendOnSuccess {
                    CommerSharedPref.userToken = data.accessToken
                    CommerSharedPref.userRefreshToken = data.refreshToken
                    token = data.accessToken
                }
                response.request.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            }
        }
    }
}