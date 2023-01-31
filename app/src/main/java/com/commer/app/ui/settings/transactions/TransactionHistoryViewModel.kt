package com.commer.app.ui.settings.transactions

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.settings.transaction.SimplerTransactionResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val getTransactionHistoryResponse = MutableLiveData<SimplerTransactionResponse>()
    suspend fun getTransactionHistoryFromServer() {
        mainRepository.getTransactionHistoryAndGetResult(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = {  },
            statusCode = { _statusCode.postValue(it) }
        ).collect {
            getTransactionHistoryResponse.postValue(it)
        }
    }

}