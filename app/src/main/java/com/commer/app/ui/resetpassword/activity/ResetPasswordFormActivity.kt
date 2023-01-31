package com.commer.app.ui.resetpassword.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordBody
import com.commer.app.data.model.remote.user.detail.DetailProfile
import com.commer.app.databinding.ActivityResetPasswordFormBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.resetpassword.viewmodel.ResetPasswordFormViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFormActivity : BaseActivity() {

    private lateinit var binding: ActivityResetPasswordFormBinding
    private val viewModel: ResetPasswordFormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnClose.setOnClickListener {
            onBackPressed()
        }

        isButtonEnabled(false)

        val getData = intent.getParcelableExtra<ForgotPasswordBody>("step2")!!

        addOnFocusAndOnTextChanged()

        binding.btnReset.setOnClickListener {
            lifecycleScope.launch {
                viewModel.postNewPasswordToServer(
                    ForgotPasswordBody(
                        binding.editConfirmPassword.text.toString(),
                        getData.email,
                        binding.editPassword.text.toString(),
                        ""
                    )
                )
            }
        }

        setupObserver()
    }

    override fun setupObserver() {
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        viewModel.newPasswordResponse.observe(this) {
            val getSettings = intent.getStringExtra("fromSettings")
            val getData = intent.getParcelableExtra<DetailProfile>("getData")
            if (it.status == "200") {
                val intent = Intent(this, ResetPasswordSuccessActivity::class.java)
                intent.putExtra("fromSettings", getSettings)
                intent.putExtra("getData", getData)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkContainPassword(): String {
        val passwordText = binding.editPassword.text.toString()
        if (
            !(passwordText.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$".toRegex()) && passwordText.isNotEmpty())
        ) {
            return getString(R.string.password_must_be).also {
                binding.txtErrorHandlingPassword.setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                )
                binding.inputPassword.boxStrokeColor =
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                binding.inputPassword.setEndIconTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(applicationContext, R.color.error_color)
                    )
                )
            }
        } else {
            return getString(R.string.password_must_be)
                .also {
                    binding.txtErrorHandlingPassword.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.text_primary)
                    )
                    binding.inputPassword.boxStrokeColor =
                        ContextCompat.getColor(applicationContext, R.color.link_color)
                    binding.inputPassword.setEndIconTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(applicationContext, R.color.text_primary)
                        )
                    )
                }
        }
    }

    private fun checkContainNewPassword(): String {
        val confirmPasswordText = binding.editConfirmPassword.text.toString()
        val passwordText = binding.editPassword.text.toString()
        if (
            !(passwordText.isEmpty() && confirmPasswordText.isEmpty())
        ) {
            if (confirmPasswordText == passwordText) {
                getBindingIfFalse()
            } else {
                return getString(R.string.password_not_match)
                    .also {
                        getBindingIfTrue()
                    }
            }
        }
        return getString(R.string.password_must_be)
            .also {
                getBindingIfFalse()
            }
    }

    private fun addOnFocusAndOnTextChanged() {
        binding.editPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.txtErrorHandlingPassword.text = checkContainPassword()
            }
        }

        binding.editConfirmPassword.doOnTextChanged { _, _, _, _ ->
            binding.txtErrorHandlingConfirmPassword.text = checkContainNewPassword()
        }
    }

    private fun isButtonEnabled(checked: Boolean): Boolean {
        if (checked) {
            return true
                .also {
                    binding.btnReset.isEnabled = it
                    binding.btnReset.setBackgroundColor(
                        ContextCompat.getColor(applicationContext, R.color.primary_color)
                    )
                    binding.btnReset.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.white)
                    )
                }
        } else {
            return false
                .also {
                    binding.btnReset.isEnabled = it
                    binding.btnReset.setBackgroundColor(
                        ContextCompat.getColor(applicationContext, R.color.text_secondary)
                    )
                    binding.btnReset.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.white)
                    )
                }
        }
    }

    private fun getBindingIfTrue() {
        binding.txtErrorHandlingConfirmPassword.setTextColor(
            ContextCompat.getColor(applicationContext, R.color.error_color)
        )
        binding.inputConfirmPassword.boxStrokeColor =
            ContextCompat.getColor(applicationContext, R.color.error_color)
        binding.inputConfirmPassword.setEndIconTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(applicationContext, R.color.error_color)
            )
        )
        isButtonEnabled(false)
    }

    private fun getBindingIfFalse() {
        binding.txtErrorHandlingConfirmPassword.setTextColor(
            ContextCompat.getColor(applicationContext, R.color.text_primary)
        )
        binding.inputConfirmPassword.boxStrokeColor =
            ContextCompat.getColor(applicationContext, R.color.link_color)
        binding.inputConfirmPassword.setEndIconTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(applicationContext, R.color.text_primary)
            )
        )
        isButtonEnabled(true)
    }
}