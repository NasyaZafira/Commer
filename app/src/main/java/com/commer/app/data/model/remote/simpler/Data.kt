package com.commer.app.data.model.remote.simpler


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("deleted_date")
    val deletedDate: Any,
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