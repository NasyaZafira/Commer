package com.commer.app.ui.register

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.signup.SignUpBody
import com.commer.app.data.model.remote.signup.SignUpResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val signUpResponse = MutableLiveData<SignUpResponse>()
    suspend fun postSignUpToServer(body: SignUpBody) {
        mainRepository.postSignUpAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            body
        ).collect {
            signUpResponse.postValue(it)
        }
    }

}