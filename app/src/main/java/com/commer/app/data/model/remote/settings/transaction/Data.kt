package com.commer.app.data.model.remote.settings.transaction


import com.google.gson.annotations.SerializedName
import java.util.*

data class Data(
    @SerializedName("created_date")
    val createdDate: Date,
    @SerializedName("deleted_date")
    val deletedDate: Date,
    @SerializedName("id_payment")
    val idPayment: Int,
    @SerializedName("id_user")
    val idUser: IdUser,
    @SerializedName("image_payment")
    val imagePayment: String,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("plan")
    val plan: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("total_paid")
    val totalPaid: String,
    @SerializedName("transaction_id")
    val transactionId: String
)