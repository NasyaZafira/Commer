package com.commer.app.data.model.remote.shop.buy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val order_id: String,
    val payment_url: String
): Parcelable