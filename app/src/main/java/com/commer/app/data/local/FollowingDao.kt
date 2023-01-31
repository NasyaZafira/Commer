package com.commer.app.data.local

import androidx.room.*
import com.commer.app.data.model.local.FollowingEntity

@Dao
interface FollowingDao {

    @Transaction
    @Query("SELECT * FROM following_user")
    fun getFollowingUser(): List<FollowingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFollowingUser(followingEntities: List<FollowingEntity>)

    @Query("DELETE FROM following_user")
    fun deleteAllFollowingUser()

}