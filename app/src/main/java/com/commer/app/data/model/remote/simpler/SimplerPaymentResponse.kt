package com.commer.app.data.model.remote.simpler


import com.google.gson.annotations.SerializedName

data class SimplerPaymentResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)