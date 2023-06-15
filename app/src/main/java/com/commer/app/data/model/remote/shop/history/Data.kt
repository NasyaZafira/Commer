package com.commer.app.data.model.remote.shop.history

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Data(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("date")
    val date: Date,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isPaid")
    val isPaid: Boolean,
    @SerializedName("productName")
    val productName: String
): Parcelable