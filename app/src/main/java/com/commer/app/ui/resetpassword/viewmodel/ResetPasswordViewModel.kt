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
class ResetPasswordViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val resetPasswordResponse = MutableLiveData<ForgotPasswordResponse>()
    suspend fun postResetPasswordToServer(body: ForgotPasswordBody) {
        mainRepository.resetPasswordAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            body
        ).collect {
            resetPasswordResponse.postValue(it)
        }
    }
}