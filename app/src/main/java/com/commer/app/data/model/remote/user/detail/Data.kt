package com.commer.app.data.model.remote.user.detail


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("detail_profile")
    val detailProfile: DetailProfile,
    @SerializedName("post_user")
    val postUser: MutableList<PostUser>
)