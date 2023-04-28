package com.commer.app.data.model.remote.shop.detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailShopResponse(
    @SerializedName("data")
    val `data`: Data,
    val message: String,
    val status: String
): Parcelable