package com.commer.app.data.model.remote.post.detail


import com.google.gson.annotations.SerializedName
import java.util.*

data class KomentarPost(
    @SerializedName("created_date")
    val createdDate: Date,
    @SerializedName("deleted_date")
    val deletedDate: Date,
    @SerializedName("id_komentar")
    val idKomentar: Int,
    @SerializedName("id_user")
    val idUser: IdUser,
    @SerializedName("isiKomentar")
    val isiKomentar: String
)