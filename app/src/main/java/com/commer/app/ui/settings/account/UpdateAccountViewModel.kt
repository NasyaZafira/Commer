package com.commer.app.ui.settings.account

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordBody
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordResponse
import com.commer.app.data.model.remote.profile.GetDetailProfileResponse
import com.commer.app.data.model.remote.settings.account.UpdateAccountBody
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class UpdateAccountViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val updateAccountResponse = MutableLiveData<GetDetailProfileResponse>()
    suspend fun updateAccountToServer(updateAccountBody: UpdateAccountBody) {
        mainRepository.updateAccountAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = {  },
            statusCode = { _statusCode.postValue(it) },
            updateAccountBody
        ).collect {
            updateAccountResponse.postValue(it)
        }
    }

    val sendCodeVerifyResponse = MutableLiveData<ForgotPasswordResponse>()
    suspend fun sendCodeVerify(forgotPasswordBody: ForgotPasswordBody) {
        mainRepository.resetPasswordAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            forgotPasswordBody
        ).collect {
            sendCodeVerifyResponse.postValue(it)
        }
    }

}