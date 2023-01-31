package com.commer.app.ui.profile.following

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.local.FollowEntity
import com.commer.app.data.model.remote.user.follow.FollowUserResponse
import com.commer.app.data.model.remote.user.unfollow.UnfollowUserResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val followingResponse = MutableLiveData<List<FollowEntity>>()
    suspend fun getFollowingUserFromServer(idUser: Int) {
        mainRepository.getFollowingUserAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idUser
        ).collect {
            followingResponse.postValue(it)
        }
    }

    // Follow User
    private val postFollowUserResponse = MutableLiveData<FollowUserResponse>()
    suspend fun postFollowUserToServer(idFollow: Int) {
        mainRepository.postFollowUserAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idFollow
        ).collect {
            postFollowUserResponse.postValue(it)
        }
    }

    // Unfollow User
    private val postUnFollowUserResponse = MutableLiveData<UnfollowUserResponse>()
    suspend fun postUnFollowUserToServer(idFollow: Int) {
        mainRepository.postUnFollowUserAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idFollow
        ).collect {
            postUnFollowUserResponse.postValue(it)
        }
    }

}