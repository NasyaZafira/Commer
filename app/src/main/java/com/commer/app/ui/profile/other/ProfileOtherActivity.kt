package com.commer.app.ui.profile.other

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.databinding.ActivityProfileOtherBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.CustomStringFormat.toGlobalMoney
import com.commer.app.ui.homepage.VideoActivity
import com.commer.app.ui.post.detail.DetailPostActivity
import com.commer.app.ui.profile.followers.FollowersActivity
import com.commer.app.ui.profile.following.FollowingActivity
import com.commer.app.ui.report.ReportActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Method

@AndroidEntryPoint
class ProfileOtherActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileOtherBinding
    private val viewModel: ProfileOtherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        val getIdUser = intent.getLongExtra("getIdUser", 0)
        getDetailUser(getIdUser.toInt())

        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        val getIdUser = intent.getLongExtra("getIdUser", 0)
        getDetailUser(getIdUser.toInt())
        binding.swipeRefresh.setOnRefreshListener {
            getDetailUser(getIdUser.toInt())
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun setupObserver() {
        viewModel.detailUserResponse.observe(this) {
            if (it.status == "200") {
                val response = it.data
                binding.linearTxtFollowing.setOnClickListener {
                    val i = Intent(this, FollowingActivity::class.java)
                    i.putExtra("getIdUser", response.detailProfile.id)
                    startActivity(i)
                }
                binding.linearTxtFollowers.setOnClickListener {
                    val i = Intent(this, FollowersActivity::class.java)
                    i.putExtra("getIdUser", response.detailProfile.id)
                    startActivity(i)
                }
                binding.btnFollow.setOnClickListener {
                    performButtonFollow(response.detailProfile.id)
                    binding.btnUnFollow.visibility = View.VISIBLE
                    binding.btnFollow.visibility = View.INVISIBLE
                }
                binding.btnUnFollow.setOnClickListener {
                    performButtonUnfollow(response.detailProfile.id)
                    binding.btnUnFollow.visibility = View.INVISIBLE
                    binding.btnFollow.visibility = View.VISIBLE
                }
                binding.imgBtnOther.setOnClickListener { view ->
                    optionsMenuProfileOtherUser(view, response.detailProfile.id)
                }
                if (response.detailProfile.profilePic.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(R.drawable.img_user)
                        .into(binding.imgProfile)
                } else {
                    Glide.with(this)
                        .load(response.detailProfile.profilePic)
                        .into(binding.imgProfile)
                }
                if (response.detailProfile.status == "Verified" ||
                    response.detailProfile.status == "Official") {
                    binding.icVerified.visibility = View.VISIBLE
                }
                if (response.detailProfile.isFollow) {
                    binding.btnUnFollow.visibility = View.VISIBLE
                    binding.btnFollow.visibility = View.INVISIBLE
                } else {
                    binding.btnUnFollow.visibility = View.INVISIBLE
                    binding.btnFollow.visibility = View.VISIBLE
                }
                val passion = response.detailProfile.passion
                val result = passion.replace("[-!$%^&*()_+|~=`{}:;'<>?,.]".toRegex(), ",")
                val finalResult = result.replace(" , ", ",").split(",")
                binding.txtFullName.text = response.detailProfile.fullname
                binding.chipPostTopicsFirst.text = finalResult.first()
                if (finalResult.size > 1) {
                    binding.chipPostTopicsSecond.visibility = View.VISIBLE
                    binding.chipPostTopicsSecond.text = finalResult.last()
                }
                binding.txtBio.text = response.detailProfile.bio
                if (response.postUser.isEmpty()) {
                    val txtTotalPost = "0"
                    binding.txtTotalPosts.text = txtTotalPost
                } else {
                    binding.txtTotalPosts.text = response.postUser.size.toGlobalMoney()
                }
                binding.txtTotalFollowers.text =
                    response.detailProfile.totalFollowers.toString()
                binding.txtTotalFollowing.text =
                    response.detailProfile.totalFollowing.toGlobalMoney()
                if (response.postUser.isEmpty()) {
                    binding.recyclerPostAtProfileOther.visibility = View.INVISIBLE
                } else {
                    binding.recyclerPostAtProfileOther.apply {
                        layoutManager = LinearLayoutManager(
                            this@ProfileOtherActivity,
                            RecyclerView.VERTICAL,
                            true
                        )
                        adapter = ProfileOtherPostAdapter(
                            response.postUser,
                            onVideoClick = { video ->
                                val i = Intent(this@ProfileOtherActivity, VideoActivity::class.java)
                                i.putExtra(VideoActivity.INTENT_VIDEO_URL, video.filePosts[0].url)
                                startActivity(i)
                            },
                            onItemClick = { item ->
                                val i = Intent(this@ProfileOtherActivity, DetailPostActivity::class.java)
                                i.putExtra("idPost", item.idPost)
                                startActivity(i)
                            },
                            object : ProfileOtherPostAdapter.OnClickedAtProfile {
                                override fun onOptionsMenuOtherUserClicked(
                                    view: View,
                                    idPost: Int,
                                    text: String,
                                    title: String,
                                    image: String?
                                ) {
                                    optionsMenuPostOtherUser(
                                        view,
                                        idPost,
                                        text,
                                        title,
                                        image
                                    )
                                }

                                override fun onBookmarkClicked(idPost: Int, position: Int) {
                                    bookmarkPost(idPost, position)
                                }

                                override fun onDeleteBookmarkClicked(idPost: Int, position: Int) {
                                    deleteBookmarkPost(idPost, position)
                                }

                                override fun onLikeClicked(idPost: Int, position: Int) {
                                    likePost(idPost, position)
                                }

                                override fun onUnlikeClicked(idPost: Int, position: Int) {
                                    unLikePost(idPost, position)
                                }
                            }
                        )
                    }
                }
            }
        }
        viewModel.postFollowUserResponse.observe(this) {
            if (it.status == "200") {
                val getIdUser = intent.getLongExtra("getIdUser", 0)
                getDetailUser(getIdUser.toInt())
            } else {
                showMessageToast(it.message)
            }
        }
        viewModel.postUnFollowUserResponse.observe(this) {
            if (it.status == "200") {
                val getIdUser = intent.getLongExtra("getIdUser", 0)
                getDetailUser(getIdUser.toInt())
            } else {
                showMessageToast(it.message)
            }
        }
        viewModel.postLikePostResponse.observe(this) {
            if (it.first.status == "200") {
                (binding.recyclerPostAtProfileOther.adapter as ProfileOtherPostAdapter?)?.apply {
                    postResponse[it.second].totalLike++
                    postResponse[it.second].liked = true
                    notifyItemChanged(it.second)
                }
            } else {
                showMessageToast(it.first.message)
            }
        }
        viewModel.postUnlikePostResponse.observe(this) {
            if (it.first.status == "200") {
                (binding.recyclerPostAtProfileOther.adapter as ProfileOtherPostAdapter?)?.apply {
                    postResponse[it.second].totalLike--
                    postResponse[it.second].liked = false
                    notifyItemChanged(it.second)
                }
            } else {
                showMessageToast(it.first.message)
            }
        }
        viewModel.postDeletePostResponse.observe(this) {
            if (it.first.status == "200") {
                val getIdUser = intent.getLongExtra("getIdUser", 0)
                getDetailUser(getIdUser.toInt())
                (binding.recyclerPostAtProfileOther.adapter as ProfileOtherPostAdapter?)?.apply {
                    if (postResponse.size != 0) {
                        postResponse.removeAt(it.second)
                        notifyItemRemoved(it.second)
                    }
                }
            } else {
                showMessageToast(it.first.message)
            }
        }
        viewModel.postBookmarkPostResponse.observe(this) {
            if (it.first.status == "200") {
                (binding.recyclerPostAtProfileOther.adapter as ProfileOtherPostAdapter?)?.apply {
                    postResponse[it.second].bookmarked = true
                    notifyItemChanged(it.second)
                }
            } else {
                showMessageToast(it.first.message)
            }
        }
        viewModel.deleteBookmarkPostResponse.observe(this) {
            if (it.first.status == "200") {
                (binding.recyclerPostAtProfileOther.adapter as ProfileOtherPostAdapter?)?.apply {
                    postResponse[it.second].bookmarked = false
                    notifyItemChanged(it.second)
                }
            } else {
                showMessageToast(it.first.message)
            }
        }
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
    }

    private fun getDetailUser(idUser: Int) {
        lifecycleScope.launch {
            viewModel.detailUserFromServer(idUser)
        }
    }

    private fun optionsMenuProfileOtherUser(view: View, idUser: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_profile_other_user)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_report -> {
                    val i = Intent(this, ReportActivity::class.java)
                    i.putExtra("getIdUser", idUser)
                    startActivity(i)
                }
            }
            false
        }

        // Show icons on popup menu
        showIconOnPopupMenu(popupMenu)
        popupMenu.show()
    }

    private fun optionsMenuPostOtherUser(
        view: View,
        idPost: Int,
        text: String,
        title: String,
        image: String?
    ) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_post_other_people)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_report -> {
                    val i = Intent(this, ReportActivity::class.java)
                    i.putExtra("getIdPost", idPost)
                    startActivity(i)
                }
                R.id.menu_share -> {
                    val share = Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "$text\n\n$image")
                        putExtra(Intent.EXTRA_TITLE, "Posted by $title")
                    }, null)
                    startActivity(share)
                }
            }
            false
        }

        // Show icons on popup menu
        showIconOnPopupMenu(popupMenu)
        popupMenu.show()
    }

    private fun performButtonFollow(userId: Int) {
        lifecycleScope.launch {
            viewModel.postFollowUserToServer(userId)
        }
    }

    private fun performButtonUnfollow(userId: Int) {
        lifecycleScope.launch {
            viewModel.postUnFollowUserToServer(userId)
        }
    }

    private fun likePost(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.postLikePostToServer(idPost, position)
        }
    }

    private fun unLikePost(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.postUnlikePostToServer(idPost, position)
        }
    }

    private fun bookmarkPost(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.postBookmarkPostToServer(idPost, position)
        }
    }

    private fun deleteBookmarkPost(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.deleteBookmarkPostToServer(idPost, position)
        }
    }

    private fun showIconOnPopupMenu(popupMenu: PopupMenu) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        } else {
            try {
                val fields = popupMenu.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[popupMenu]
                        val classPopupHelper =
                            Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons: Method = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}