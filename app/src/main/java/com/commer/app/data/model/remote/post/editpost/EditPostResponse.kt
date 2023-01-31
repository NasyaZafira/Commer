package com.commer.app.data.model.remote.post.editpost

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditPostResponse(
    @SerializedName("id_post")
    val idPost: Int,
    @SerializedName("post_desc")
    val postDesc: String,
    @SerializedName("post_status")
    val postStatus: Boolean,
    @SerializedName("post_tags")
    val postTags: List<String>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?,
) : Parcelable