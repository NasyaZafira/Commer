package com.commer.app.data.model.remote.report.post


import com.google.gson.annotations.SerializedName

data class ReportPostResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)