package com.commer.app.ui.resetpassword.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.commer.app.base.BaseActivity
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordBody
import com.commer.app.data.model.remote.user.detail.DetailProfile
import com.commer.app.databinding.ActivityCodeVerifyBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.resetpassword.viewmodel.CodeVerifyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class CodeVerifyActivity : BaseActivity() {

    private lateinit var binding: ActivityCodeVerifyBinding
    private val viewModel: CodeVerifyViewModel by viewModels()
    private var countClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCodeVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnClose.setOnClickListener {
            onBackPressed()
        }

        val getData = intent.getParcelableExtra<ForgotPasswordBody>("step1")!!
        binding.txtEmailAddress.text = getData.email

        binding.pinViewCode.doOnTextChanged { text, _, _, _ ->
            val getText = text.toString()
            binding.btnNext.isEnabled = getText.isNotEmpty() && getText.length == 4
        }

        val resendTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.txtResendCode.visibility = View.VISIBLE
                binding.txtResendCodeBlue.visibility = View.INVISIBLE
                val f = DecimalFormat("00")
                val min = (millisUntilFinished / 60000) % 60
                val sec = (millisUntilFinished / 1000) % 60
                val text = "Resend Code ${f.format(min)}:${f.format(sec)}"
                binding.txtResendCode.text = text
            }

            override fun onFinish() {
                binding.txtResendCode.visibility = View.INVISIBLE
                binding.txtResendCodeBlue.visibility = View.VISIBLE
            }
        }
        resendTimer.start()

        binding.txtResendCodeBlue.setOnClickListener {
            lifecycleScope.launch {
                viewModel.postResendPasswordToServer(
                    ForgotPasswordBody(
                        "",
                        getData.email,
                        "",
                        binding.pinViewCode.text.toString()
                    )
                )
            }
            binding.pinViewCode.isEnabled = true
            binding.pinViewCode.text = null
            resendTimer.start()
        }

        binding.btnNext.setOnClickListener {
            lifecycleScope.launch {
                viewModel.postCodeVerifyToServer(
                    ForgotPasswordBody(
                        "",
                        getData.email,
                        "",
                        binding.pinViewCode.text.toString()
                    )
                )
            }
            if (countClick++ > 1) {
                resendTimer.cancel()
                binding.pinViewCode.isEnabled = false
                val resendTimerClick = object : CountDownTimer(3600000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.txtResendCode.visibility = View.VISIBLE
                        binding.txtResendCodeBlue.visibility = View.INVISIBLE
                        val f = DecimalFormat("00")
                        val hour = (millisUntilFinished / 3600000) % 24
                        val min = (millisUntilFinished / 60000) % 60
                        val sec = (millisUntilFinished / 1000) % 60
                        val text = "Resend Code ${f.format(hour)}:${f.format(min)}:${f.format(sec)}"
                        binding.txtResendCode.text = text
                    }

                    override fun onFinish() {
                        binding.txtResendCode.visibility = View.INVISIBLE
                        binding.txtResendCodeBlue.visibility = View.VISIBLE
                    }
                }
                resendTimerClick.start()
            }
        }

        setupObserver()
    }

    override fun setupObserver() {
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        viewModel.codeVerifyResponse.observe(this) {
            val getSettings = intent.getStringExtra("fromSettings")
            val getData = intent.getParcelableExtra<DetailProfile>("getData")
            if (it.status == "200") {
                val intent = Intent(this, ResetPasswordFormActivity::class.java)
                intent.putExtra("fromSettings", getSettings)
                intent.putExtra("getData", getData)
                intent.putExtra(
                    "step2", ForgotPasswordBody(
                        "",
                        binding.txtEmailAddress.text.toString(),
                        "",
                        binding.pinViewCode.text.toString()
                    )
                )
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                binding.pinViewCode.text = null
            }
        }
        viewModel.resendPasswordResponse.observe(this) {
            if (it.status != "200") {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}