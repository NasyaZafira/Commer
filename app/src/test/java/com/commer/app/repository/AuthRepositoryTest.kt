package com.commer.app.repository

import app.cash.turbine.test
import com.commer.app.MainCoroutinesRule
import com.commer.app.data.model.remote.login.LoginResponse
import com.commer.app.data.remote.ApiService
import com.commer.app.utils.MockUtil.mockLogin
import com.commer.app.utils.MockUtil.mockLoginResponse
import com.nhaarman.mockitokotlin2.*
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalCoroutinesApi
@ExperimentalTime
class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private val service: ApiService = mock()

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        authRepository = AuthRepository(service, Dispatchers.IO)
    }

    @Test
    fun postLoginUserToNetworkTest() = runBlocking {
        val mockBody = mockLogin()
        val mockData = LoginResponse(mockLoginResponse(), "success", "200")
        whenever(service.login(mockBody)).thenReturn(ApiResponse.of { Response.success(mockData) })

        authRepository.userLogin(
            onStart = {},
            onComplete = {},
            onError = {},
            mockBody
        ).test(2.toDuration(DurationUnit.SECONDS)) {
            val mockLogin = mockLoginResponse()
            val expectedItem = awaitItem()

            assertEquals(expectedItem.data.user, mockLogin.user)
            awaitComplete()
        }

        verify(service, atLeastOnce()).login(mockBody)
        verifyNoMoreInteractions(service)
    }

}