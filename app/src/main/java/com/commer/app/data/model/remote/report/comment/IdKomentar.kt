package com.commer.app.data.model.remote.report.comment


import com.google.gson.annotations.SerializedName

data class IdKomentar(
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("deleted_date")
    val deletedDate: Any,
    @SerializedName("id_komentar")
    val idKomentar: Int,
    @SerializedName("id_user")
    val idUser: IdUser,
    @SerializedName("isiKomentar")
    val isiKomentar: String
)