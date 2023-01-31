package com.commer.app.data.local

import androidx.room.*
import com.commer.app.data.model.local.FollowersEntity

@Dao
interface FollowersDao {

    @Transaction
    @Query("SELECT * FROM followers_user")
    fun getFollowersUser(): List<FollowersEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFollowersUser(followersEntity: List<FollowersEntity>)

    @Query("DELETE FROM followers_user")
    fun deleteAllFollowersUser()

}