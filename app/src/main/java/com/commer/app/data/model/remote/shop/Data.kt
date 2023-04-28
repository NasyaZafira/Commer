package com.commer.app.data.model.remote.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val id: Int,
    val images: String,
    val name: String,
    val price: String
): Parcelable