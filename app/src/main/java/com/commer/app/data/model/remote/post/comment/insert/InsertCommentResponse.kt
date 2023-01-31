package com.commer.app.data.model.remote.post.comment.insert


import com.google.gson.annotations.SerializedName

data class InsertCommentResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)