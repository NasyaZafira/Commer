package com.commer.app.ui.shop

import androidx.lifecycle.MutableLiveData
import com.commer.app.base.BaseViewModel
import com.commer.app.data.model.remote.shop.ListShopResponse
import com.commer.app.data.model.remote.shop.buy.BuyResponse
import com.commer.app.data.model.remote.shop.detail.DetailShopResponse
import com.commer.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopRepository: MainRepository
): BaseViewModel() {
    val listProduct = MutableLiveData<ListShopResponse>()
    val detailProduct = MutableLiveData<DetailShopResponse>()
    val buy = MutableLiveData<BuyResponse>()

    suspend fun listProduct(){
        shopRepository.listProduct(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
        ).collect {
            listProduct.postValue(it)
        }
    }

    suspend fun getDetailProduct(
        id : Int
    ){
        shopRepository.getDetail(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            id
        ).collect {
            detailProduct.postValue(it)
        }
    }

    suspend fun buyProduct(
        id : Int
    ){
        shopRepository.buyProduct(
            onStart = { _loading.postValue(true) },
            onComplete = { _loading.postValue(false) },
            onError = { _message.postValue(it) },
            id
        ).collect {
            buy.postValue(it)
        }
    }
}