package com.commer.app.data.model.remote.login


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginBody(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
) : Parcelable