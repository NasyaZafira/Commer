package com.commer.app.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.user.detail.DetailProfile
import com.commer.app.databinding.ActivitySettingsBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.main.SplashActivity
import com.commer.app.ui.settings.account.UpdateAccountActivity
import com.commer.app.ui.settings.bookmarks.BookmarksActivity
import com.commer.app.ui.settings.contact.ContactUsActivity
import com.commer.app.ui.settings.profile.UpdateProfileActivity
import com.commer.app.ui.settings.transactions.TransactionHistoryActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    private val userId = CommerSharedPref.userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnLogout.setOnClickListener {
            LogoutDialog().show(supportFragmentManager, "LogoutDialog")
        }

        val getData = intent.getParcelableExtra<DetailProfile>("getData")!!
        dataProfile(getData)
    }

    override fun onRestart() {
        super.onRestart()

        loadingUI = CustomLoadingDialog(this)

        lifecycleScope.launch {
            viewModel.detailUserFromServer(userId!!)
        }

        setupObserver()
    }

    override fun setupObserver() {
        viewModel.detailUserResponse.observe(this) {
            if (it.status == "200") {
                dataProfile(it.data.detailProfile)
            }
        }
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
    }

    private fun dataProfile(getData: DetailProfile) {
        if (getData.status == "Subscribed") {
            binding.icOneEllipse.visibility = View.VISIBLE
            binding.txtSubscribed.visibility = View.VISIBLE
        }
        if (getData.profilePic != null) {
            Glide.with(this)
                .load(getData.profilePic)
                .into(binding.imgPeople)
        }
        binding.txtFullName.text = getData.fullname
        val passion = getData.passion
        val result = passion.replace("[-!$%^&*()_+|~=`{}:;'<>?,.]".toRegex(), ",")
        val finalResult = result.replace(" , ", ",").split(",")
        binding.txtPeopleInterest.text = finalResult.first()

        binding.constraintProfile.setOnClickListener {
            val i = Intent(this, UpdateProfileActivity::class.java)
            i.putExtra("getData", getData)
            startActivity(i)
        }

        binding.constraintAccount.setOnClickListener {
            val i = Intent(this, UpdateAccountActivity::class.java)
            i.putExtra("getData", getData)
            startActivity(i)
        }

        binding.constraintBookmarks.setOnClickListener {
            val i = Intent(this, BookmarksActivity::class.java)
            startActivity(i)
        }

        binding.constraintTransaction.setOnClickListener {
            val i = Intent(this, TransactionHistoryActivity::class.java)
            startActivity(i)
        }

        binding.constraintContactUs.setOnClickListener {
            val i = Intent(this, ContactUsActivity::class.java)
            startActivity(i)
        }

        binding.constraintPrivacyPolicy.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://websitecommit.herokuapp.com/privacy"))
            startActivity(i)
        }
    }

    fun logout() {
        CommerSharedPref.clear()
        val i = Intent(this, SplashActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finishAffinity()
    }
}