package com.commer.app.data.model.remote.post.like


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("deleted_date")
    val deletedDate: Any,
    @SerializedName("id_like")
    val idLike: Int,
    @SerializedName("id_post")
    val idPost: Int,
    @SerializedName("id_user")
    val idUser: IdUser
)