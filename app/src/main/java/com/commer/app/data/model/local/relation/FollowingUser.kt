package com.commer.app.data.model.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.commer.app.data.model.local.FollowEntity
import com.commer.app.data.model.local.FollowingEntity

data class FollowingUser(
    @Embedded
    val followingEntity: FollowingEntity,
    @Relation(
        parentColumn = "follow_id",
        entityColumn = "id"
    )
    val follow: List<FollowEntity>
)
