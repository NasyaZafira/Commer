package com.commer.app.data.model.remote.report.post


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("deleted_date")
    val deletedDate: Any,
    @SerializedName("id_post")
    val idPost: IdPost,
    @SerializedName("id_report")
    val idReport: Int,
    @SerializedName("id_user")
    val idUser: IdUser,
    @SerializedName("reason")
    val reason: String
)