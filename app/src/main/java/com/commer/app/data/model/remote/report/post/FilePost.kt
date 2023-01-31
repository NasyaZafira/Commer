package com.commer.app.data.model.remote.report.post


import com.google.gson.annotations.SerializedName

data class FilePost(
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("deleted_date")
    val deletedDate: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("url")
    val url: String
)