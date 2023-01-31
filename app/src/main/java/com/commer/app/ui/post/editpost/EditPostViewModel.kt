package com.commer.app.ui.post.editpost

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.post.editpost.EditPostResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val editPostResponse = MutableLiveData<EditPostResponse>()
    suspend fun editPostToServer(idPost: Int, status: Boolean, tags: String, desc: String) {
        val statusRequestBody = status.toString().toRequestBody("text/plain".toMediaType())
        val tagsRequestBody = tags.toRequestBody("text/plain".toMediaType())
        val descRequestBody = desc.toRequestBody("text/plain".toMediaType())
        mainRepository.editPostAndGetResult(
            onStart = {
                _loading.postValue(true)
                _notification.postValue(true)
            },
            onComplete = {
                _loading.postValue(false)
                _notification.postValue(false)
            },
            onError = { _message.postValue(it) },
            statusCode = { _statusCode.postValue(it) },
            idPost = idPost,
            status = statusRequestBody,
            tags = tagsRequestBody,
            desc = descRequestBody
        ).collect {
            editPostResponse.postValue(it)
        }
    }

}