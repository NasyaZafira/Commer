package com.commer.app.ui.settings.profile

import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.profile.GetDetailProfileResponse
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
class UpdateProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val updateProfileResponse = MutableLiveData<GetDetailProfileResponse>()
    suspend fun updateProfileToServer(fullName: String, bio: String, files: List<File>?) {
        val filesMultipart = files?.map {
            val name = it.name.replace(" ", "").lowercase(Locale.ROOT)
            val fileRequestBody = it.asRequestBody(getMimeType(it.path.replace(" ", ""))!!.toMediaType())
            return@map MultipartBody.Part.createFormData(
                "file",
                name, fileRequestBody
            )
        }
        val fullNameRequestBody = fullName.toRequestBody("text/plain".toMediaType())
        val bioRequestBody = bio.toRequestBody("text/plain".toMediaType())
        mainRepository.updateProfileGetResult(
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
            fullName = fullNameRequestBody,
            bio = bioRequestBody,
            file = filesMultipart,
        ).collect {
            updateProfileResponse.postValue(it)
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