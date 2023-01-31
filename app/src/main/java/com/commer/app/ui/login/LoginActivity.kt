package com.commer.app.ui.login

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.login.LoginBody
import com.commer.app.databinding.ActivityLoginBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.main.MainActivity
import com.commer.app.ui.register.SignUpFormActivity
import com.commer.app.ui.resetpassword.activity.ResetPasswordActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.arrowBack.setOnClickListener { onBackPressed() }

        binding.edtEmail.doAfterTextChanged {
            isButtonEnabled()
        }

        binding.edtPassword.doAfterTextChanged {
            isButtonEnabled()
        }

        binding.txtForgotPassword.setOnClickListener {
            val i = Intent(this, ResetPasswordActivity::class.java)
            startActivity(i)
        }

        binding.btnLogin.setOnClickListener { login() }

        binding.txtGoToSignUp.setOnClickListener {
            val i = Intent(this, SignUpFormActivity::class.java)
            startActivity(i)
        }

        setupObserver()
    }

    private fun isButtonEnabled() {
        val isEmailValid = checkContainEmail()
        val isPasswordValid = checkContainPassword()
        binding.btnLogin.isEnabled = isEmailValid && isPasswordValid
    }

    private fun checkContainEmail(): Boolean {
        val emailText = binding.edtEmail.text.toString()
        return when {
            !(Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) -> {
                val message =
                    getString(R.string.something_wrong_email).also { getBindingEmailFalse() }
                binding.txtEmailErrorHandling.text = message
                binding.txtEmailErrorHandling.visibility = View.VISIBLE
                false
            }
            emailText.isEmpty() -> {
                val message =
                    getString(R.string.email_address_in_format).also { getBindingEmailFalse() }
                binding.txtEmailErrorHandling.text = message
                binding.txtEmailErrorHandling.visibility = View.VISIBLE
                false
            }
            else -> {
                binding.layoutEmail.boxStrokeColor =
                    ContextCompat.getColor(applicationContext, R.color.link_color)
                        .also { getBindingEmailTrue() }
                binding.txtEmailErrorHandling.visibility = View.GONE
                true
            }
        }
    }

    private fun checkContainPassword(): Boolean {
        val passwordText = binding.edtPassword.text.toString()
        return if (
            (passwordText.isEmpty() || passwordText.length < 6)
        ) {
            val message =
                getString(R.string.password_must_be).also { getBindingPasswordFalse() }
            binding.txtPasswordErrorHandling.text = message
            binding.txtPasswordErrorHandling.visibility = View.VISIBLE
            false
        } else {
            binding.layoutPassword.boxStrokeColor =
                ContextCompat.getColor(applicationContext, R.color.link_color)
                    .also { getBindingPasswordTrue() }
            binding.txtPasswordErrorHandling.visibility = View.GONE
            true
        }
    }

    private fun login() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        when {
            email.isEmpty() -> {
                binding.edtEmail.error = "Email required"
                binding.edtEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.edtPassword.error = "Password required"
                binding.edtPassword.requestFocus()
            }
            else -> {
                lifecycleScope.launch {
                    viewModel.postLoginToServer(body = LoginBody(email, password))
                }
            }
        }
    }

    override fun setupObserver() {
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.loading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        viewModel.loginResponse.observe(this) {
            when (it.message) {
                "user not found" -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    val message =
                        getString(R.string.we_cant_find_an_account).also { getBindingEmailFalse() }
                    binding.txtEmailErrorHandling.text = message
                    binding.txtEmailErrorHandling.visibility = View.VISIBLE
                }
                "wrong password" -> {
                    Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show()
                    val message =
                        getString(R.string.incorrect_password_please).also { getBindingPasswordFalse() }
                    binding.txtPasswordErrorHandling.text = message
                    binding.txtPasswordErrorHandling.visibility = View.VISIBLE
                }
                else -> {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()
                    val i = Intent(this, MainActivity::class.java)
                    CommerSharedPref.userToken = it.data.accessToken
                    CommerSharedPref.userRefreshToken = it.data.refreshToken
                    CommerSharedPref.userId = it.data.user.id
                    CommerSharedPref.userEmail = it.data.user.username
                    CommerSharedPref.userPicture = it.data.user.profilePic
                    CommerSharedPref.userStatus = it.data.user.status
                    CommerSharedPref.isLoggedIn = true
                    startActivity(i)
                    finish()
                }
            }
        }


    }

    private fun getBindingEmailFalse() {
        binding.layoutEmail.isEndIconVisible = false
        binding.txtEmailErrorHandling.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.error_color
            )
        )
        binding.layoutEmail.boxStrokeColor =
            ContextCompat.getColor(applicationContext, R.color.error_color)
        binding.txtEmailErrorHandling.visibility = View.VISIBLE
    }

    private fun getBindingEmailTrue() {
        binding.layoutEmail.isEndIconVisible = true
        binding.layoutEmail.endIconDrawable =
            ContextCompat.getDrawable(applicationContext, R.drawable.ic_tick)
        binding.layoutEmail.setEndIconTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.success_color
                )
            )
        )
        binding.txtEmailErrorHandling.visibility = View.GONE
    }

    private fun getBindingPasswordFalse() {
        binding.txtPasswordErrorHandling.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.error_color
            )
        )
        binding.layoutPassword.boxStrokeColor =
            ContextCompat.getColor(applicationContext, R.color.error_color)
        binding.layoutPassword.setEndIconTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.error_color
                )
            )
        )
        binding.txtPasswordErrorHandling.visibility = View.VISIBLE
    }

    private fun getBindingPasswordTrue() {
        binding.layoutPassword.setEndIconTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.text_primary
                )
            )
        )
        binding.txtPasswordErrorHandling.visibility = View.GONE
    }

}