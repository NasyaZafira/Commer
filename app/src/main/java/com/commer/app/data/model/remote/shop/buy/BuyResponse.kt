package com.commer.app.data.model.remote.shop.buy

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BuyResponse(
    @SerializedName("data")
    val `data`: Data,
    val message: String,
    val status: String
): Parcelable