package com.commer.app.network

import com.commer.app.MainCoroutinesRule
import com.commer.app.data.remote.ApiService
import com.commer.app.utils.MockUtil.mockLogin
import com.commer.app.utils.MockUtil.mockSignUp
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CommitServiceTest : ApiAbstract<ApiService>() {

    private lateinit var service: ApiService

    @get:Rule
    var coroutineRule = MainCoroutinesRule()

    @Before
    fun initService() {
        service = createService(ApiService::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun postSignUpUserToNetworkTest() = runBlocking {
        enqueueResponse("/SignUpResponse.json")
        val mockData = mockSignUp()
        val response = service.postSignUp(mockData)
        val responseBody = requireNotNull((response as ApiResponse.Success).data)
        mockWebServer.takeRequest()

        assertThat(responseBody.status, `is`("200"))
    }

    @Throws(IOException::class)
    @Test
    fun postLoginUserToNetworkTest() = runBlocking {
        enqueueResponse("/LoginResponse.json")
        val mockData = mockLogin()
        val response = service.login(mockData)
        val responseBody = requireNotNull((response as ApiResponse.Success).data)
        mockWebServer.takeRequest()

        assertThat(responseBody.status, `is`("200"))
    }

}