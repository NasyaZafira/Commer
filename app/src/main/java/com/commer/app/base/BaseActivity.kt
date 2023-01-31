package com.commer.app.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.commer.app.BuildConfig
import com.commer.app.ui.BottomSheetError
import com.commer.app.ui.CustomLoadingDialog

abstract class BaseActivity:  AppCompatActivity() {

    private var isAlertShow = false
    protected lateinit var loadingUI: CustomLoadingDialog
    protected lateinit var errorMessageUI: BottomSheetError

    override fun onStart() {
        super.onStart()
        if (BuildConfig.DEBUG) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setupObserver()
    }

    protected fun showLoading() {
        loadingUI.show()
    }

    protected fun hideLoading() {
        loadingUI.dismiss()
    }

    protected fun showMessageToast(msg: String?) {
        msg?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    protected fun showMessageBottomSheet(manager: FragmentManager, tags: String) {
        errorMessageUI.show(manager, tags)
    }

    protected abstract fun setupObserver()

}
