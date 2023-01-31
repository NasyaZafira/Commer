package com.commer.app.data.model.remote.forgotpassword


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgotPasswordBody(
    @SerializedName("confirmNewPassword")
    val confirmNewPassword: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("otp")
    val otp: String
) : Parcelable