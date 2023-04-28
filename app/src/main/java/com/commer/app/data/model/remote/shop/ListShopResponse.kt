package com.commer.app.data.model.remote.shop

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListShopResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    val message: String,
    val status: String
): Parcelable