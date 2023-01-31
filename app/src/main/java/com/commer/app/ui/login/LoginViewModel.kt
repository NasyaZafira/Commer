package com.commer.app.ui.login

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.login.LoginBody
import com.commer.app.data.model.remote.login.LoginResponse
import com.commer.app.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    val loginResponse = MutableLiveData<LoginResponse>()

    suspend fun postLoginToServer(body: LoginBody) {
        authRepository.userLogin(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            body
        ).collect {
            loginResponse.postValue(it)
        }
    }

}