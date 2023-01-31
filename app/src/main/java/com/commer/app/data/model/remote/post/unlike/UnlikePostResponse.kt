package com.commer.app.data.model.remote.post.unlike


import com.google.gson.annotations.SerializedName

data class UnlikePostResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)