package com.commer.app.data.model.remote.post.delete

import com.google.gson.annotations.SerializedName

data class DeletePostResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)