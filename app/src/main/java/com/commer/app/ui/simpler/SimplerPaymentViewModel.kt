package com.commer.app.ui.simpler

import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.simpler.CheckPaymentResponse
import com.commer.app.data.model.remote.simpler.SimplerPaymentResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel

class SimplerPaymentViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    // Create Simpler Payment Transaction
    val simplerPaymentResponse = MutableLiveData<SimplerPaymentResponse>()
    suspend fun postSimplerPaymentToServer(
        plan: String,
        transaction_id: String,
        files: List<File>?
    ) {
        val filesMultipart = files?.map {
            val fileRequestBody = it.asRequestBody(getMimeType(it.path)!!.toMediaType())
            return@map MultipartBody.Part.createFormData(
                "file",
                it.name, fileRequestBody
            )
        }
        val planRequestBody = plan.toRequestBody("text/plain".toMediaType())
        val transactionIdRequestBody =
            transaction_id.toRequestBody("text/plain".toMediaType())
        mainRepository.postSimplerReceipt(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            statusCode = { _statusCode.postValue(it) },
            plan = planRequestBody,
            transaction_id = transactionIdRequestBody,
            file = filesMultipart,
        ).collect {
            simplerPaymentResponse.postValue(it)
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

    // Get Simpler Payment Status
    val getPaymentStatusResponse = MutableLiveData<CheckPaymentResponse>()
    suspend fun getPaymentStatusFromServer() {
        mainRepository.getPaymentStatus(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            statusCode = { _statusCode.postValue(it) }
        ).collect {
            getPaymentStatusResponse.postValue(it)
        }
    }

    suspend fun getDetailUser() {
        val userId = CommerSharedPref.userId
        mainRepository.detailUserAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { },
            userId!!
        ).collect { responseUser ->
            CommerSharedPref.userStatus = responseUser.data.detailProfile.status
        }
    }

}