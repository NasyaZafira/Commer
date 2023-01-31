package com.commer.app.data.model.remote.bookmark

import com.google.gson.annotations.SerializedName

data class BookmarkResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)