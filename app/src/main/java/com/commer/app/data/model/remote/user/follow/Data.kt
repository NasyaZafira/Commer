package com.commer.app.data.model.remote.user.follow


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("deleted_date")
    val deletedDate: String,
    @SerializedName("id_follow")
    val idFollow: Int,
    @SerializedName("id_user")
    val idUser: IdUser,
    @SerializedName("id_user_following")
    val idUserFollowing: IdUserFollowing,
    @SerializedName("is_follow")
    val isFollow: Boolean
)