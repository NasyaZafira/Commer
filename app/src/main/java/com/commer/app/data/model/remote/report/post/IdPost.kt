package com.commer.app.data.model.remote.report.post


import com.google.gson.annotations.SerializedName

data class IdPost(
    @SerializedName("bookmarked")
    val bookmarked: Boolean,
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("deleted_date")
    val deletedDate: Any,
    @SerializedName("filePosts")
    val filePosts: List<FilePost>,
    @SerializedName("id_post")
    val idPost: Int,
    @SerializedName("liked")
    val liked: Boolean,
    @SerializedName("post_desc")
    val postDesc: String,
    @SerializedName("post_status")
    val postStatus: Boolean,
    @SerializedName("post_tags")
    val postTags: List<String>,
    @SerializedName("total_komentar")
    val totalKomentar: Int,
    @SerializedName("total_like")
    val totalLike: Int,
    @SerializedName("user")
    val user: User
)