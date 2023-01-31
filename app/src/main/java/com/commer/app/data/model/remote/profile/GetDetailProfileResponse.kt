package com.commer.app.data.model.remote.profile


import com.google.gson.annotations.SerializedName

data class GetDetailProfileResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)