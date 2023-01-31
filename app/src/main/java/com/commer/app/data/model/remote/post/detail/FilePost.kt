package com.commer.app.data.model.remote.post.detail


import com.google.gson.annotations.SerializedName
import java.util.*

data class FilePost(
    @SerializedName("created_date")
    val createdDate: Date,
    @SerializedName("deleted_date")
    val deletedDate: Date,
    @SerializedName("id")
    val id: Int,
    @SerializedName("url")
    val url: String
)