package com.commer.app.data.model.remote.post.get


import com.google.gson.annotations.SerializedName

data class GetPostResponse(
    @SerializedName("data")
    val `data`: MutableList<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)