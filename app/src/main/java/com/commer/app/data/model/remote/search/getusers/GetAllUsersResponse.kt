package com.commer.app.data.model.remote.search.getusers


import com.google.gson.annotations.SerializedName

data class GetAllUsersResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)