package com.commer.app.data.model.remote.post.newpost


import com.google.gson.annotations.SerializedName

data class NewPostResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)