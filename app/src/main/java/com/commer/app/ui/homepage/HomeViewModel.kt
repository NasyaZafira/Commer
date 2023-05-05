package com.commer.app.ui.homepage

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.bookmark.BookmarkResponse
import com.commer.app.data.model.remote.post.delete.DeletePostResponse
import com.commer.app.data.model.remote.post.get.GetPostResponse
import com.commer.app.data.model.remote.post.like.LikePostResponse
import com.commer.app.data.model.remote.post.unlike.UnlikePostResponse
import com.commer.app.data.model.remote.user.follow.FollowUserResponse
import com.commer.app.data.model.remote.user.suggested.SuggestedUserResponse
import com.commer.app.data.model.remote.user.unfollow.UnfollowUserResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    // Get All Post by Following User
    val postResponse = MutableLiveData<GetPostResponse>()
    suspend fun getPostFromServer(tags: String?) {
        mainRepository.getAllPosts(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it)},
            onException = {_errorMessage.postValue(true) },
            statusCode = { _statusCode.postValue(it) },
            tags
        ).collect {
            postResponse.postValue(it)
        }
        val userId = CommerSharedPref.userId
        mainRepository.detailUserAndGetResult(
            onStart = { },
            onComplete = { },
            onError = { },
            userId!!
        ).collect { responseUser ->
            CommerSharedPref.userStatus = responseUser.data.detailProfile.status
        }
    }

    // Get Suggested User
    val getSuggestedUserResponse = MutableLiveData<SuggestedUserResponse>()
    suspend fun getSuggestedUserFromServer() {
        mainRepository.getSuggestedUser(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            onException = { _errorMessage.postValue(true) },
        ).collect {
            getSuggestedUserResponse.postValue(it)
        }
    }

    // Follow User
    val postFollowUserResponse = MutableLiveData<FollowUserResponse>()
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
    val postUnFollowUserResponse = MutableLiveData<UnfollowUserResponse>()
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

    // Like Post
    val postLikePostResponse = MutableLiveData<Pair<LikePostResponse, Int>>()
    suspend fun postLikePostToServer(idPost: Int, position: Int) {
        mainRepository.postLikePostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            postLikePostResponse.postValue(Pair(it, position))
        }
    }

    // Unlike Post
    val postUnlikePostResponse = MutableLiveData<Pair<UnlikePostResponse, Int>>()
    suspend fun postUnlikePostToServer(idPost: Int, position: Int) {
        mainRepository.postUnlikePostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            postUnlikePostResponse.postValue(Pair(it, position))
        }
    }

    // Delete Post
    val postDeletePostResponse = MutableLiveData<Pair<DeletePostResponse, Int>>()
    suspend fun postDeletePostToServer(idPost: Int, position: Int) {
        mainRepository.postDeletePostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            postDeletePostResponse.postValue(Pair(it, position))
        }
    }

    // Bookmark Post
    val postBookmarkPostResponse = MutableLiveData<Pair<BookmarkResponse, Int>>()
    suspend fun postBookmarkPostToServer(idPost: Int, position: Int) {
        mainRepository.postBookmarkPostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            postBookmarkPostResponse.postValue(Pair(it, position))
        }
    }

    // Delete Bookmark Post
    val deleteBookmarkPostResponse = MutableLiveData<Pair<BookmarkResponse, Int>>()
    suspend fun deleteBookmarkPostToServer(idPost: Int, position: Int) {
        mainRepository.deleteBookmarkPostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            deleteBookmarkPostResponse.postValue(Pair(it, position))
        }
    }

}