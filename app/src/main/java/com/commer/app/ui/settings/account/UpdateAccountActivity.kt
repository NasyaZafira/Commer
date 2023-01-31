package com.commer.app.ui.settings.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordBody
import com.commer.app.data.model.remote.settings.account.UpdateAccountBody
import com.commer.app.data.model.remote.user.detail.DetailProfile
import com.commer.app.databinding.ActivityUpdateAccountBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.resetpassword.activity.CodeVerifyActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdateAccountActivity : BaseActivity() {

    private lateinit var binding: ActivityUpdateAccountBinding
    private val viewModel: UpdateAccountViewModel by viewModels()
    private var textDomicile = ""
    private var textGender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityUpdateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        val getData = intent.getParcelableExtra<DetailProfile>("getData")!!
        binding.editEmail.setText(getData.username)
        binding.editPhoneNumber.setText(getData.phoneNumber)
        binding.autoCompleteTxtDomicile.setText(getData.region)
        binding.autoCompleteTxtGender.setText(getData.gender)
        binding.editPassword.setText(R.string.new_password)

        binding.autoCompleteTxtDomicile.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                binding.txtErrorHandlingDomicile.visibility = View.VISIBLE
            } else {
                textDomicile = it.toString()
            }
            binding.btnSave.isEnabled = textDomicile.isNotEmpty() || textGender.isNotEmpty()
        }

        binding.autoCompleteTxtGender.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                binding.txtErrorHandlingGender.visibility = View.VISIBLE
            } else {
                textGender = it.toString()
            }
            binding.btnSave.isEnabled = textDomicile.isNotEmpty() || textGender.isNotEmpty()
        }

        binding.txtResetPassword.setOnClickListener {
            lifecycleScope.launch {
                viewModel.sendCodeVerify(
                    ForgotPasswordBody(
                        "",
                        getData.username,
                        "",
                        ""
                    )
                )
            }
        }

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                binding.btnSave.isEnabled = false
                viewModel.updateAccountToServer(
                    UpdateAccountBody(
                        getData.username,
                        getData.phoneNumber,
                        binding.autoCompleteTxtDomicile.text.toString(),
                        binding.autoCompleteTxtGender.text.toString()
                    )
                )
            }
        }

        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        val domicile = resources.getStringArray(R.array.domicile)
        val arrayAdapterDomicile = ArrayAdapter(this, R.layout.signup_menu, domicile)
        binding.autoCompleteTxtDomicile.setAdapter(arrayAdapterDomicile)

        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapterGender = ArrayAdapter(this, R.layout.signup_menu, gender)
        binding.autoCompleteTxtGender.setAdapter(arrayAdapterGender)
    }

    override fun setupObserver() {
        viewModel.updateAccountResponse.observe(this) {
            if (it.status == "200") {
                showMessageToast("Success")
            } else {
                showMessageToast(it.message)
            }
        }
        viewModel.sendCodeVerifyResponse.observe(this) {
            val getData = intent.getParcelableExtra<DetailProfile>("getData")!!
            if (it.status == "200") {
                val i = Intent(this, CodeVerifyActivity::class.java)
                i.putExtra("fromSettings", "settings")
                i.putExtra("getData", getData)
                i.putExtra("step1", ForgotPasswordBody(
                    "",
                    binding.editEmail.text.toString(),
                    "",
                    ""
                ))
                startActivity(i)
                finish()
            } else {
                showMessageToast(it.message)
            }
        }
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
    }
}