package com.commer.app.data.model.remote.settings.transaction


import com.google.gson.annotations.SerializedName

data class SimplerTransactionResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)