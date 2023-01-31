package com.commer.app.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "follow")
@Parcelize
data class FollowEntity(
    @ColumnInfo(name = "authorities")
    @SerializedName("authorities")
    val authorities: String?,
    @ColumnInfo(name = "bio")
    @SerializedName("bio")
    val bio: String?,
    @ColumnInfo(name = "fullname")
    @SerializedName("fullname")
    val fullname: String,
    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    val gender: String,
    @ColumnInfo(name = "id")
    @PrimaryKey
    @SerializedName("id")
    val id: Long,
    @ColumnInfo(name = "is_follow")
    @SerializedName("is_follow")
    val isFollow: Boolean,
    @ColumnInfo(name = "passion")
    @SerializedName("passion")
    val passion: String,
    @ColumnInfo(name = "phone_number")
    @SerializedName("phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "profile_pic")
    @SerializedName("profile_pic")
    val profilePic: String?,
    @ColumnInfo(name = "region")
    @SerializedName("region")
    val region: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    val status: String,
    @ColumnInfo(name = "total_followers")
    @SerializedName("total_followers")
    val totalFollowers: Int,
    @ColumnInfo(name = "total_following")
    @SerializedName("total_following")
    val totalFollowing: Int,
    @ColumnInfo(name = "username")
    @SerializedName("username")
    val username: String
) : Parcelable