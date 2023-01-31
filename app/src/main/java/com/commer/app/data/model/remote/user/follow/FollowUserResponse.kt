package com.commer.app.data.model.remote.user.follow


import com.google.gson.annotations.SerializedName

data class FollowUserResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)