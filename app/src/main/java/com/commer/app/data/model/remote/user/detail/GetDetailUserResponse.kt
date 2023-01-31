package com.commer.app.data.model.remote.user.detail


import com.google.gson.annotations.SerializedName

data class GetDetailUserResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)