package com.commer.app.data.model.remote.user.unfollow


import com.google.gson.annotations.SerializedName

data class UnfollowUserResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)