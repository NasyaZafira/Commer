package com.commer.app.data.model.remote.post.get


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Data(
    @SerializedName("created_date")
    val createdDate: Date,
    @SerializedName("deleted_date")
    val deletedDate: String?,
    @SerializedName("filePosts")
    val filePosts: List<FilePost>,
    @SerializedName("id_post")
    val idPost: Int,
    @SerializedName("liked")
    var liked: Boolean,
    @SerializedName("bookmarked")
    var bookmarked: Boolean,
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
) : Parcelable