package com.commer.app.ui.register

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.commer.app.R
import com.commer.app.data.model.remote.signup.SignUpBody
import com.commer.app.databinding.ActivitySignUpFormBinding
import com.commer.app.ui.login.LoginActivity

class SignUpFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnNext.isEnabled = false
        editTextOnFocusListener()

        binding.imgBtnClose.setOnClickListener {
            onBackPressed()
        }

        binding.btnNext.setOnClickListener {
            signUp()
        }
    }

    override fun onResume() {
        super.onResume()
        val domicile = resources.getStringArray(R.array.domicile)
        val arrayAdapterDomicile = ArrayAdapter(this, R.layout.signup_menu, domicile)
        val autoCompleteDomicile = binding.autoCompleteTxtDomicile
        autoCompleteDomicile.setAdapter(arrayAdapterDomicile)

        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapterGender = ArrayAdapter(this, R.layout.signup_menu, gender)
        val autoCompleteGender = binding.autoCompleteTxtGender
        autoCompleteGender.setAdapter(arrayAdapterGender)
    }

    private fun isButtonEnabled() {
        binding.txtErrorHandlingFullName.text = checkContainFullName()
        binding.txtErrorHandlingEmail.text = checkContainEmail()
        binding.txtErrorHandlingPhoneNumber.text = checkContainPhoneNumber()
        binding.txtErrorHandlingDomicile.text = checkContainDomicile()
        binding.txtErrorHandlingGender.text = checkContainGender()
        binding.txtErrorHandlingPassword.text = checkContainPassword()
        checkBoxTerms()

        val checkContainFullName = binding.txtErrorHandlingFullName.visibility == View.GONE
        val checkContainEmail = binding.txtErrorHandlingEmail.visibility == View.GONE
        val checkContainPhoneNumber = binding.txtErrorHandlingPhoneNumber.visibility == View.GONE
        val checkContainDomicile = binding.txtErrorHandlingDomicile.visibility == View.GONE
        val checkContainGender = binding.txtErrorHandlingGender.visibility == View.GONE
        val checkContainPassword = binding.txtErrorHandlingPassword.visibility == View.GONE
        val checkBoxTerms = binding.checkboxTerms.isChecked

        if (checkContainEmail && checkContainPhoneNumber &&
            checkContainPassword && checkBoxTerms && checkContainFullName &&
            checkContainDomicile && checkContainGender
        ) {
            binding.btnNext.isEnabled = true
            binding.btnNext.setBackgroundColor(
                ContextCompat.getColor(applicationContext, R.color.primary_color)
            )
            binding.btnNext.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.white)
            )
        } else {
            binding.btnNext.isEnabled = false
            binding.btnNext.setBackgroundColor(
                ContextCompat.getColor(applicationContext, R.color.text_secondary)
            )
            binding.btnNext.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.white)
            )
            Toast.makeText(this, "Invalid Form", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkContainFullName(): String? {
        val fullNameText = binding.editFullName.text.toString()
        if (
            !(fullNameText.matches("^(?![\\s.]+\$)[a-zA-Z\\s.]{1,100}\$".toRegex()) && fullNameText.isNotEmpty())
        ) {
            return getString(R.string.something_wrong_name).also {
                binding.txtErrorHandlingFullName.setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                )
                binding.inputFullName.boxStrokeColor =
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                binding.txtErrorHandlingFullName.visibility = View.VISIBLE
                binding.inputFullName.isEndIconVisible = false
            }
        } else {
            binding.inputFullName.boxStrokeColor =
                ContextCompat.getColor(applicationContext, R.color.link_color).also {
                    binding.inputFullName.isEndIconVisible = true
                    binding.inputFullName.endIconDrawable =
                        ContextCompat.getDrawable(applicationContext, R.drawable.ic_user)
                    binding.inputFullName.setEndIconTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(applicationContext, R.color.text_secondary)
                        )
                    )
                    binding.txtErrorHandlingFullName.visibility = View.GONE
                }
        }
        return null
    }

    private fun checkContainPassword(): String? {
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
                binding.txtErrorHandlingPassword.visibility = View.VISIBLE
            }
        } else {
            binding.inputPassword.boxStrokeColor =
                ContextCompat.getColor(applicationContext, R.color.link_color).also {
                    binding.inputPassword.setEndIconTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(applicationContext, R.color.text_primary)
                        )
                    )
                    binding.txtErrorHandlingPassword.visibility = View.GONE
                }
        }
        return null
    }

    private fun checkContainEmail(): String? {
        val emailText = binding.editEmail.text.toString()
        if (
            !(Patterns.EMAIL_ADDRESS.matcher(emailText).matches() && emailText.isNotEmpty())
        ) {
            return getString(R.string.something_wrong_email).also {
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
            }
        } else {
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
                }
        }
        return null
    }

    private fun checkContainPhoneNumber(): String? {
        val phoneNumberText = binding.editPhoneNumber.text.toString()
        if (
            !(phoneNumberText.matches("^(^\\+62|62)(\\d{3,4}-?){2}\\d{3,4}\$".toRegex()) && phoneNumberText.isNotEmpty())
        ) {
            return getString(R.string.something_wrong_phone).also {
                binding.inputPhoneNumber.isEndIconVisible = false
                binding.txtErrorHandlingPhoneNumber.setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                )
                binding.inputPhoneNumber.boxStrokeColor =
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                binding.txtErrorHandlingPhoneNumber.visibility = View.VISIBLE
            }
        } else {
            binding.inputPhoneNumber.boxStrokeColor =
                ContextCompat.getColor(applicationContext, R.color.link_color).also {
                    binding.inputPhoneNumber.isEndIconVisible = true
                    binding.inputPhoneNumber.endIconDrawable =
                        ContextCompat.getDrawable(applicationContext, R.drawable.ic_tick)
                    binding.inputPhoneNumber.setEndIconTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(applicationContext, R.color.success_color)
                        )
                    )
                    binding.txtErrorHandlingPhoneNumber.visibility = View.GONE
                }
        }
        return null
    }

    private fun checkContainDomicile(): String? {
        val domicileText = binding.autoCompleteTxtDomicile.text.toString()
        if (domicileText.isEmpty()) {
            return getString(R.string.please_choose_domicile).also {
                binding.txtErrorHandlingDomicile.setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                )
                binding.inputDomicile.boxStrokeColor =
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                binding.txtErrorHandlingDomicile.visibility = View.VISIBLE
            }
        } else {
            binding.inputDomicile.boxStrokeColor =
                ContextCompat.getColor(applicationContext, R.color.link_color)
            binding.txtErrorHandlingDomicile.visibility = View.GONE
        }
        return null
    }

    private fun checkContainGender(): String? {
        val genderText = binding.autoCompleteTxtGender.text.toString()
        if (genderText.isEmpty()) {
            return getString(R.string.please_choose_gender).also {
                binding.txtErrorHandlingGender.setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                )
                binding.inputGender.boxStrokeColor =
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                binding.txtErrorHandlingGender.visibility = View.VISIBLE
            }
        } else {
            binding.inputGender.boxStrokeColor =
                ContextCompat.getColor(applicationContext, R.color.link_color)
            binding.txtErrorHandlingGender.visibility = View.GONE
        }
        return null
    }

    private fun checkBoxTerms() {
        val termsCheck = binding.checkboxTerms
        if (!termsCheck.isChecked) {
            binding.checkboxTerms.buttonTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.error_color
                )
            )
        } else {
            binding.checkboxTerms.buttonTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.primary_color
                )
            )
        }
    }

    private fun signUp() {
        binding.txtErrorHandlingFullName.text = checkContainFullName()
        binding.txtErrorHandlingEmail.text = checkContainEmail()
        binding.txtErrorHandlingPhoneNumber.text = checkContainPhoneNumber()
        binding.txtErrorHandlingDomicile.text = checkContainDomicile()
        binding.txtErrorHandlingGender.text = checkContainGender()
        binding.txtErrorHandlingPassword.text = checkContainPassword()
        checkBoxTerms()

        val checkContainFullName = binding.txtErrorHandlingFullName.visibility == View.GONE
        val checkContainEmail = binding.txtErrorHandlingEmail.visibility == View.GONE
        val checkContainPhoneNumber = binding.txtErrorHandlingPhoneNumber.visibility == View.GONE
        val checkContainDomicile = binding.txtErrorHandlingDomicile.visibility == View.GONE
        val checkContainGender = binding.txtErrorHandlingGender.visibility == View.GONE
        val checkContainPassword = binding.txtErrorHandlingPassword.visibility == View.GONE
        val checkBoxTerms = binding.checkboxTerms.isChecked

        if (
            checkContainEmail && checkContainPhoneNumber &&
            checkContainPassword && checkBoxTerms && checkContainFullName &&
            checkContainDomicile && checkContainGender
        ) {
            val intent = Intent(this, SignUpInterestsActivity::class.java)
            intent.putExtra(
                "signUp", SignUpBody(
                    binding.autoCompleteTxtDomicile.text.toString(),
                    binding.editEmail.text.toString(),
                    binding.autoCompleteTxtGender.text.toString(),
                    binding.editFullName.text.toString(),
                    binding.editPassword.text.toString(),
                    binding.editPhoneNumber.text.toString(),
                    "null"
                )
            )
            startActivity(intent)
            resetToDefault()
        } else {
            Toast.makeText(this, "Invalid Form", Toast.LENGTH_LONG).show()
        }
    }

    private fun resetToDefault() {
        binding.inputFullName.boxStrokeColor = ContextCompat.getColor(
            applicationContext, R.color.link_color
        )
        binding.inputEmail.boxStrokeColor = ContextCompat.getColor(
            applicationContext, R.color.link_color
        )
        binding.inputPhoneNumber.boxStrokeColor = ContextCompat.getColor(
            applicationContext, R.color.link_color
        )
        binding.inputDomicile.boxStrokeColor = ContextCompat.getColor(
            applicationContext, R.color.link_color
        )
        binding.inputGender.boxStrokeColor = ContextCompat.getColor(
            applicationContext, R.color.link_color
        )
        binding.inputPassword.boxStrokeColor = ContextCompat.getColor(
            applicationContext, R.color.link_color
        )
    }

    private fun editTextOnFocusListener() {
        binding.editFullName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.txtErrorHandlingFullName.text = checkContainFullName()
            }
        }

        binding.editEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.txtErrorHandlingEmail.text = checkContainEmail()
            }
        }

        binding.editPhoneNumber.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.txtErrorHandlingPhoneNumber.text = checkContainPhoneNumber()
            }
        }

        binding.autoCompleteTxtDomicile.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.txtErrorHandlingDomicile.text = checkContainDomicile()
            }
        }

        binding.autoCompleteTxtGender.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.txtErrorHandlingGender.text = checkContainGender()
            }
        }

        binding.editPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.txtErrorHandlingPassword.text = checkContainPassword()
            }
        }

        binding.checkboxTerms.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                binding.checkboxTerms.buttonTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(applicationContext, R.color.error_color)
                )
                binding.btnNext.isEnabled = false
                binding.btnNext.setBackgroundColor(
                    ContextCompat.getColor(applicationContext, R.color.text_secondary)
                )
                binding.btnNext.setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.white)
                )
            } else {
                binding.checkboxTerms.buttonTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(applicationContext, R.color.primary_color)
                )
                isButtonEnabled()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    Log.e("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
            if (v is AutoCompleteTextView) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    Log.e("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}