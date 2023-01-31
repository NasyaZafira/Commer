package com.commer.app.data.model.remote.signup


import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)