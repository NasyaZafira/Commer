package com.commer.app.data.model.remote.user.followers

import com.commer.app.data.model.remote.user.Users
import com.google.gson.annotations.SerializedName

data class FollowersResponse(
    @SerializedName("data")
    val data: List<Users>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)
