package com.commer.app.data.model.remote.report.comment


import com.google.gson.annotations.SerializedName

data class ReportCommentResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)