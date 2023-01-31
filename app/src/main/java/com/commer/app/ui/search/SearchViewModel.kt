package com.commer.app.ui.search

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.search.getusers.GetAllUsersResponse
import com.commer.app.data.model.remote.user.follow.FollowUserResponse
import com.commer.app.data.model.remote.user.unfollow.UnfollowUserResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    // Get All User
    val getAllUsersResponse = MutableLiveData<GetAllUsersResponse>()
    suspend fun getUsersFromServer(fullname: String?) {
        mainRepository.getAllUser(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { },
            statusCode = { _statusCode.postValue(it) },
            fullname
        ).collect {
            getAllUsersResponse.postValue(it)
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