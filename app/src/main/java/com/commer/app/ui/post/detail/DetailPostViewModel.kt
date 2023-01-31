package com.commer.app.ui.post.detail

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.bookmark.BookmarkResponse
import com.commer.app.data.model.remote.post.detail.DetailPostResponse
import com.commer.app.repository.MainRepository
import com.commer.app.data.model.remote.post.comment.delete.DeleteCommentResponse
import com.commer.app.data.model.remote.post.comment.insert.InsertCommentResponse
import com.commer.app.data.model.remote.post.delete.DeletePostResponse
import com.commer.app.data.model.remote.post.like.LikePostResponse
import com.commer.app.data.model.remote.post.unlike.UnlikePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class DetailPostViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    // Get Detail Post
    val detailPostResponse = MutableLiveData<DetailPostResponse>()
    suspend fun getDetailPostFromServer(idPost: Int) {
        mainRepository.getDetailPostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = {  },
            idPost
        ).collect {
            detailPostResponse.postValue(it)
        }
    }

    // Like Post
    val postLikePostResponse = MutableLiveData<LikePostResponse>()
    suspend fun postLikePostToServer(idPost: Int) {
        mainRepository.postLikePostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            postLikePostResponse.postValue(it)
        }
    }

    // Unlike Post
    val postUnlikePostResponse = MutableLiveData<UnlikePostResponse>()
    suspend fun postUnlikePostToServer(idPost: Int) {
        mainRepository.postUnlikePostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            postUnlikePostResponse.postValue(it)
        }
    }

    // Delete Post
    val postDeletePostResponse = MutableLiveData<DeletePostResponse>()
    suspend fun postDeletePostToServer(idPost: Int) {
        mainRepository.postDeletePostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            postDeletePostResponse.postValue(it)
        }
    }

    // Bookmark Post
    val postBookmarkPostResponse = MutableLiveData<BookmarkResponse>()
    suspend fun postBookmarkPostToServer(idPost: Int) {
        mainRepository.postBookmarkPostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            postBookmarkPostResponse.postValue(it)
        }
    }

    // Delete Bookmark Post
    val deleteBookmarkPostResponse = MutableLiveData<BookmarkResponse>()
    suspend fun deleteBookmarkPostToServer(idPost: Int) {
        mainRepository.deleteBookmarkPostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost
        ).collect {
            deleteBookmarkPostResponse.postValue(it)
        }
    }

    // Insert Comment
    val postCommentResponse = MutableLiveData<InsertCommentResponse>()
    suspend fun postCommentToServer(idPost: Int, fieldComment: String) {
        mainRepository.postCommentAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = {  },
            idPost,
            fieldComment
        ).collect {
            postCommentResponse.postValue(it)
        }
    }

    // Delete Comment
    val deleteCommentResponse = MutableLiveData<Pair<DeleteCommentResponse, Int>>()
    suspend fun deleteCommentToServer(idComment: Int, position: Int) {
        mainRepository.deleteCommentAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = {  },
            idComment
        ).collect {
            deleteCommentResponse.postValue(Pair(it, position))
        }
    }

}