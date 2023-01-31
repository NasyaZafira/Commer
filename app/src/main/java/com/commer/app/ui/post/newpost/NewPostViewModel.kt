package com.commer.app.ui.post.newpost

import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.post.newpost.NewPostResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val newPostResponse = MutableLiveData<NewPostResponse>()
    suspend fun postNewPostToServer(status: Boolean, tags: String, desc: String, files: List<File>?) {
        val filesMultipart = files?.map {
            val name = it.name.replace(" ", "").lowercase(Locale.ROOT)
            val fileRequestBody = it.asRequestBody(getMimeType(it.path.replace(" ", ""))!!.toMediaType())
            return@map MultipartBody.Part.createFormData(
                "file",
                name, fileRequestBody
            )
        }
        val statusRequestBody = status.toString().toRequestBody("text/plain".toMediaType())
        val tagsRequestBody = tags.toRequestBody("text/plain".toMediaType())
        val descRequestBody = desc.toRequestBody("text/plain".toMediaType())
        mainRepository.postNewPostAndGetResult(
            onStart = {
                _loading.postValue(true)
                _notification.postValue(true)
                      },
            onComplete = {
                _loading.postValue(false)
                _notification.postValue(false)
                         },
            onError = {
                _message.postValue(it)
                _notificationError.postValue(true)
                      },
            statusCode = { _statusCode.postValue(it) },
            status = statusRequestBody,
            tags = tagsRequestBody,
            desc = descRequestBody,
            file = filesMultipart,
        ).collect {
            newPostResponse.postValue(it)
        }
    }

    private fun getMimeType(path: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

}