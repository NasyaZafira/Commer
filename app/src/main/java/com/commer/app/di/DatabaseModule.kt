package com.commer.app.di

import android.app.Application
import com.commer.app.data.local.CommerDatabase
import com.commer.app.data.local.FollowDao
import com.commer.app.data.local.FollowingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): CommerDatabase {
        return CommerDatabase.instance(application)
    }

    @Provides
    @Singleton
    fun provideFollowDao(commerDatabase: CommerDatabase): FollowDao {
        return commerDatabase.followDao()
    }

    @Provides
    @Singleton
    fun provideFollowingDao(commerDatabase: CommerDatabase): FollowingDao {
        return commerDatabase.followingDao()
    }

}