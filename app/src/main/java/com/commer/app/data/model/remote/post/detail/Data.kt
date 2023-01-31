package com.commer.app.data.model.remote.post.detail


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("detail_post")
    val detailPost: DetailPost,
    @SerializedName("komentar_post")
    val komentarPost: MutableList<KomentarPost>
)