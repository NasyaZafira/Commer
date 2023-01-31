package com.commer.app.data.model.remote.post.detail


import com.google.gson.annotations.SerializedName

data class DetailPostResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)