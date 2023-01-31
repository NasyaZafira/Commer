package com.commer.app.ui.resetpassword.viewmodel

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordBody
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class CodeVerifyViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val codeVerifyResponse = MutableLiveData<ForgotPasswordResponse>()
    suspend fun postCodeVerifyToServer(body: ForgotPasswordBody) {
        mainRepository.codeVerifyAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            body
        ).collect {
            codeVerifyResponse.postValue(it)
        }
    }

    val resendPasswordResponse = MutableLiveData<ForgotPasswordResponse>()
    suspend fun postResendPasswordToServer(body: ForgotPasswordBody) {
        mainRepository.resendPasswordAndGetResult(
            onStart = {  },
            onComplete = {  },
            onError = { _message.postValue(it) },
            body
        ).collect {
            resendPasswordResponse.postValue(it)
        }
    }
}