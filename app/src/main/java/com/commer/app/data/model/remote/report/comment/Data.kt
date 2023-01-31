package com.commer.app.data.model.remote.report.comment


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("deleted_date")
    val deletedDate: Any,
    @SerializedName("id_komentar")
    val idKomentar: IdKomentar,
    @SerializedName("id_report_comment")
    val idReportComment: Int,
    @SerializedName("id_user")
    val idUser: IdUserX,
    @SerializedName("reason")
    val reason: String
)