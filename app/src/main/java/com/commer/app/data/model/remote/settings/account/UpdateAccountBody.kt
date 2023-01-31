package com.commer.app.data.model.remote.settings.account

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateAccountBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("domicile")
    val domicile: String,
    @SerializedName("gender")
    val gender: String
) : Parcelable
