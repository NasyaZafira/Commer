package com.commer.app.ui.report

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.report.comment.ReportCommentResponse
import com.commer.app.data.model.remote.report.post.ReportPostResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val reportPostResponse = MutableLiveData<ReportPostResponse>()
    suspend fun reportPostToServer(idPost: Int, reason: String) {
        mainRepository.reportPostAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idPost,
            reason
        ).collect {
            reportPostResponse.postValue(it)
        }
    }

    val reportCommentResponse = MutableLiveData<ReportCommentResponse>()
    suspend fun reportCommentToServer(idComment: Int, reason: String) {
        mainRepository.reportCommentAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idComment,
            reason
        ).collect {
            reportCommentResponse.postValue(it)
        }
    }

    val reportUserResponse = MutableLiveData<ReportCommentResponse>()
    suspend fun reportUserToServer(idUser: Int, reason: String) {
        mainRepository.reportUserAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idUser,
            reason
        ).collect {
            reportUserResponse.postValue(it)
        }
    }

}