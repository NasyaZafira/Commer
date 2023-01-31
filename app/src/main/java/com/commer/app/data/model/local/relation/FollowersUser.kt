package com.commer.app.data.model.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.commer.app.data.model.local.FollowEntity
import com.commer.app.data.model.local.FollowersEntity

data class FollowersUser(
    @Embedded
    val followersEntity: FollowersEntity,
    @Relation(
        parentColumn = "follow_id",
        entityColumn = "id"
    )
    val follow: List<FollowEntity>
)
