package com.commer.app.data.model.remote.post.detail


import com.google.gson.annotations.SerializedName
import java.util.*

data class DetailPost(
    @SerializedName("bookmarked")
    val bookmarked: Boolean,
    @SerializedName("created_date")
    val createdDate: Date,
    @SerializedName("deleted_date")
    val deletedDate: Date,
    @SerializedName("filePosts")
    val filePosts: List<FilePost>,
    @SerializedName("id_post")
    val idPost: Int,
    @SerializedName("liked")
    var liked: Boolean,
    @SerializedName("post_desc")
    val postDesc: String,
    @SerializedName("post_status")
    val postStatus: Boolean,
    @SerializedName("post_tags")
    val postTags: List<String>,
    @SerializedName("total_komentar")
    val totalKomentar: Int,
    @SerializedName("total_like")
    var totalLike: Int,
    @SerializedName("user")
    val user: User
)