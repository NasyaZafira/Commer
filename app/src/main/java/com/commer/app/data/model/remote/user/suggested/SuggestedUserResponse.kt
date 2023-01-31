package com.commer.app.data.model.remote.user.suggested


import com.google.gson.annotations.SerializedName

data class SuggestedUserResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)