package com.commer.app.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "following_user",
    foreignKeys = [
        ForeignKey(
            entity = FollowEntity::class,
            parentColumns = ["id"],
            childColumns = ["follow_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class FollowingEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "follow_id", index = true)
    val followId: Long
) : Parcelable
