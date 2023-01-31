package com.commer.app.ui.resetpassword.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordBody
import com.commer.app.databinding.ActivityResetPasswordBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.resetpassword.viewmodel.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        isButtonEnabled(false)

        binding.imgBtnClose.setOnClickListener {
            onBackPressed()
        }

        binding.editEmail.doOnTextChanged { _, _, _, _ ->
            binding.txtErrorHandlingEmail.text = checkContainEmail()
        }

        binding.btnNext.setOnClickListener {
            lifecycleScope.launch {
                viewModel.postResetPasswordToServer(
                    ForgotPasswordBody(
                        "",
                        binding.editEmail.text.toString(),
                        "",
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
        viewModel.resetPasswordResponse.observe(this) {
            if (it.status == "200") {
                val intent = Intent(this, CodeVerifyActivity::class.java)
                intent.putExtra("step1", ForgotPasswordBody(
                    "",
                    binding.editEmail.text.toString(),
                    "",
                    ""
                ))
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                binding.editEmail.text = null
            }
        }
    }

    private fun checkContainEmail(): String? {
        val emailText = binding.editEmail.text.toString()
        if (
            !(Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        ) {
            if (emailText.isEmpty()) {
                return getString(R.string.email_address_in_format).also {
                    getBindingIfTrue()
                }
            }
            return getString(R.string.something_wrong_email).also {
                getBindingIfTrue()
            }
        } else {
            getBindingIfFalse()
        }
        return null
    }

    private fun isButtonEnabled(checked: Boolean): Boolean {
        if (checked) {
            return checked == true
                .also {
                    binding.btnNext.isEnabled = it
                    binding.btnNext.setBackgroundColor(
                        ContextCompat.getColor(applicationContext, R.color.primary_color)
                    )
                    binding.btnNext.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.white)
                    )
                }
        } else {
            return checked == false
                .also {
                    binding.btnNext.isEnabled = it
                    binding.btnNext.setBackgroundColor(
                        ContextCompat.getColor(applicationContext, R.color.text_secondary)
                    )
                    binding.btnNext.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.white)
                    )
                }
        }
    }

    private fun getBindingIfTrue() {
        binding.inputEmail.isEndIconVisible = false
        binding.txtErrorHandlingEmail.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.error_color
            )
        )
        binding.inputEmail.boxStrokeColor =
            ContextCompat.getColor(applicationContext, R.color.error_color)
        binding.txtErrorHandlingEmail.visibility = View.VISIBLE
        isButtonEnabled(false)
    }

    private fun getBindingIfFalse() {
        binding.inputEmail.boxStrokeColor =
            ContextCompat.getColor(applicationContext, R.color.link_color).also {
                binding.inputEmail.isEndIconVisible = true
                binding.inputEmail.endIconDrawable =
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_tick)
                binding.inputEmail.setEndIconTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(applicationContext, R.color.success_color)
                    )
                )
                binding.txtErrorHandlingEmail.visibility = View.GONE
                isButtonEnabled(true)
            }
    }
}