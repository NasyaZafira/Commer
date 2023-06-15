package com.commer.app.data.model.remote.shop.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryShopResponse(
    val `data`: List<Data>,
    val message: String,
    val status: String
): Parcelable