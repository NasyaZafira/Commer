package com.commer.app.data.model.remote.signup


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("authorities")
    val authorities: Any,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_follow")
    val isFollow: Boolean,
    @SerializedName("passion")
    val passion: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("profile_pic")
    val profilePic: String?,
    @SerializedName("region")
    val region: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("total_followers")
    val totalFollowers: Int,
    @SerializedName("total_following")
    val totalFollowing: Int,
    @SerializedName("username")
    val username: String
)