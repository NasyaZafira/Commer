package com.commer.app.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import com.commer.app.base.BaseActivity
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.signup.SignUpBody
import com.commer.app.databinding.ActivitySignUpInterestsBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.main.MainActivity
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpInterestsActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpInterestsBinding
    private val viewModel: SignUpViewModel by viewModels()
    private var selectedInterest = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpInterestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.chipsInterests.forEach {
            (it as? Chip)?.setOnCheckedChangeListener { button, checked ->
                if (checked) {
                    selectedInterest.add(button.text.toString())
                } else {
                    var findAt = -1
                    selectedInterest.forEachIndexed { index, interest ->
                        if (interest == button.text.toString()) {
                            findAt = index
                        }
                    }
                    if (findAt != -1) {
                        selectedInterest.removeAt(findAt)
                    }
                }
                checkButton()
            }
        }

        val getData = intent.getParcelableExtra<SignUpBody>("signUp")!!
        binding.btnSignUp.setOnClickListener {
            val interest = selectedInterest.joinToString(",")
            lifecycleScope.launch {
                viewModel.postSignUpToServer(
                    SignUpBody(
                        getData.domicile,
                        getData.email,
                        getData.gender,
                        getData.name,
                        getData.password,
                        getData.phoneNumber,
                        interest
                    )
                )
            }
        }

        setupObserver()
    }

    private fun checkButton() {
        if (selectedInterest.size > 2 && selectedInterest.isNotEmpty())
            showMessageToast("Sorry, maximum 2 interests")
        binding.btnSignUp.isEnabled = selectedInterest.size == 2 || selectedInterest.size == 1
    }

    override fun setupObserver() {
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.signUpResponse.observe(this) {
            if (it.status == "200") {
                val i = Intent(this, MainActivity::class.java)
                CommerSharedPref.userToken = it.data.data.accessToken
                CommerSharedPref.userRefreshToken = it.data.data.refreshToken
                CommerSharedPref.userId = it.data.data.user.id
                CommerSharedPref.userEmail = it.data.data.user.username
                CommerSharedPref.userPicture = it.data.data.user.profilePic
                CommerSharedPref.userStatus = it.data.data.user.status
                CommerSharedPref.isLoggedIn = true
                startActivity(i)
                finish()
            } else {
                showMessageToast(it.message)
                onBackPressed()
            }
        }
    }
}