package com.commer.app.utils

import com.commer.app.data.model.remote.login.Data
import com.commer.app.data.model.remote.login.LoginBody
import com.commer.app.data.model.remote.login.User
import com.commer.app.data.model.remote.signup.SignUpBody

object MockUtil {

    fun mockLogin() = LoginBody(
        email = "test@commit.com",
        password = "test123"
    )

    private fun mockUserData() = User(
        authorities = "",
        bio = null,
        fullname = "Hello Test",
        gender = "Male",
        id = 1,
        passion = "Android",
        phoneNumber = "01234567890",
        profilePic = null,
        region = "WAKANDA",
        status = "User",
        totalFollowers = 1000000,
        totalFollowing = 1,
        username = "test@commit.com"
    )

    fun mockLoginResponse() = Data(
        accessToken = "test123",
        expiresIn = 1,
        jti = "test123",
        refreshToken = "test123456",
        scope = "test123",
        tokenType = "Bearer",
        mockUserData()
    )

    fun mockSignUp() = SignUpBody(
        name = "Hello Test",
        email = "test@commit.com",
        gender = "Male",
        domicile = "WAKANDA",
        password = "test123",
        phoneNumber = "01234567890",
        interest = "Android"
    )

}