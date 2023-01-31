package com.commer.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.commer.app.data.model.local.FollowEntity
import com.commer.app.data.model.local.FollowersEntity
import com.commer.app.data.model.local.FollowingEntity

@Database(
    entities = [
        FollowEntity::class,
        FollowingEntity::class,
        FollowersEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CommerDatabase : RoomDatabase() {

    abstract fun followDao(): FollowDao
    abstract fun followingDao(): FollowingDao
    abstract fun followersDao(): FollowersDao

    companion object {
        private var database: CommerDatabase? = null

        fun instance(context: Context): CommerDatabase {
            if (database == null) {
                synchronized(CommerDatabase::class) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        CommerDatabase::class.java,
                        "commer_database.db"
                    ).build()
                }
            }
            return database!!
        }

        fun destroyInstance() {
            database = null
        }
    }

}