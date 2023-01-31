package com.commer.app.data.model.remote.post.like


import com.google.gson.annotations.SerializedName

data class LikePostResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)