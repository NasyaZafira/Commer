package com.commer.app.ui.settings

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.user.detail.GetDetailUserResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val detailUserResponse = MutableLiveData<GetDetailUserResponse>()
    suspend fun detailUserFromServer(idUser: Int) {
        mainRepository.detailUserAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            idUser
        ).collect {
            detailUserResponse.postValue(it)
        }
    }

}