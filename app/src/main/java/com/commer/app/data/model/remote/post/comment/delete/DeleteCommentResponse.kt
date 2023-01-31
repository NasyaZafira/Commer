package com.commer.app.data.model.remote.post.comment.delete


import com.google.gson.annotations.SerializedName

data class DeleteCommentResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)