package com.commer.app.ui.settings.bookmarks

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.bookmark.BookmarkResponse
import com.commer.app.data.model.remote.post.delete.DeletePostResponse
import com.commer.app.data.model.remote.post.get.GetPostResponse
import com.commer.app.data.model.remote.post.like.LikePostResponse
import com.commer.app.data.model.remote.post.unlike.UnlikePostResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    // Get All Post by Following User
    val getBookmarksResponse = MutableLiveData<GetPostResponse>()
    suspend fun getBookmarksFromServer() {
        mainRepository.getBookmarksAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = {  },
            statusCode = { _statusCode.postValue(it) }
        ).collect {
            getBookmarksResponse.postValue(it)
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