package com.commer.app.data.local

import android.app.Application
import com.orhanobut.hawk.Hawk

object CommerSharedPref {
    private const val USER_TOKEN = "userToken"
    private const val USER_REFRESH_TOKEN = "userRefreshToken"
    private const val USER_ID = "userId"
    private const val USER_EMAIL = "userEmail"
    private const val USER_PICTURE = "userPicture"
    private const val USER_STATUS = "userStatus"
    private const val IS_LOGIN = "isLogin"

    fun appInit(application: Application) {
        Hawk.init(application).build()
    }

    var userToken: String? = null
        get() = Hawk.get(USER_TOKEN)
        set(value) {
            Hawk.put(USER_TOKEN, value)
            field = value
        }

    var userRefreshToken: String? = null
        get() = Hawk.get(USER_REFRESH_TOKEN)
        set(value) {
            Hawk.put(USER_REFRESH_TOKEN, value)
            field = value
        }

    var userId: Int? = null
        get() = Hawk.get(USER_ID)
        set(value) {
            Hawk.put(USER_ID, value)
            field = value
        }

    var userEmail: String? = null
        get() = Hawk.get(USER_EMAIL)
        set(value) {
            Hawk.put(USER_EMAIL, value)
            field = value
        }

    var userPicture: String? = null
        get() = Hawk.get(USER_PICTURE)
        set(value) {
            Hawk.put(USER_PICTURE, value)
            field = value
        }

    var userStatus: String? = null
        get() = Hawk.get(USER_STATUS)
        set(value) {
            Hawk.put(USER_STATUS, value)
            field = value
        }

    var isLoggedIn: Boolean = false
        get() = Hawk.get(IS_LOGIN, false)
        set(value) {
            Hawk.put(IS_LOGIN, value)
            field = value
        }

    fun clear() {
        Hawk.deleteAll()
    }
}