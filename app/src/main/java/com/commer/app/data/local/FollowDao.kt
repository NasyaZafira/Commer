package com.commer.app.data.local

import androidx.room.*
import com.commer.app.data.model.local.FollowEntity

@Dao
interface FollowDao {

    @Query("SELECT * FROM follow")
    fun getAllFollow(): List<FollowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFollow(followEntities: List<FollowEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFollow(followEntity: FollowEntity)

    @Delete
    fun deleteFollow(followEntity: FollowEntity)

}