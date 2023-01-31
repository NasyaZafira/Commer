package com.commer.app.ui.post.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.post.editpost.EditPostResponse
import com.commer.app.databinding.ActivityDetailPostBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.TimeAgo.toTimeAgo
import com.commer.app.ui.homepage.VideoActivity
import com.commer.app.ui.post.editpost.EditPostActivity
import com.commer.app.ui.profile.other.ProfileOtherActivity
import com.commer.app.ui.report.ReportActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Method

@AndroidEntryPoint
class DetailPostActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailPostBinding
    private val viewModel: DetailPostViewModel by viewModels()
    private val userId = CommerSharedPref.userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val getSimpler = intent.getStringExtra("simpler")
        if (getSimpler != null) {
            setTheme(R.style.Theme_Commit_Simpler_Post)
        } else {
            setTheme(R.style.Theme_Commit_Home)
        }
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }
        val getIdPost = intent.getIntExtra("idPost", 0)

        getDetailPost(getIdPost)
        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        val getIdPost = intent.getIntExtra("idPost", 0)
        getDetailPost(getIdPost)
    }

    override fun setupObserver() {
        viewModel.detailPostResponse.observe(this) {
            val getIdUser = CommerSharedPref.userId
            val getSimpler = intent.getStringExtra("simpler")
            val detailPost = it.data.detailPost
            if (it.status == "200") {
                binding.txtTotalLove.text = it.data.detailPost.totalLike.toString()
                binding.txtTotalComment.text = it.data.detailPost.totalKomentar.toString()
                binding.btnMore.setOnClickListener { view ->
                    val dataPost = EditPostResponse(
                        detailPost.idPost,
                        detailPost.postDesc,
                        detailPost.postStatus,
                        detailPost.postTags,
                        null,
                        null
                    )
                    if (detailPost.user.id == getIdUser) {
                        if (getSimpler != null) {
                            optionsMenuMyDetailSimplerPost(
                                view,
                                detailPost.idPost,
                                dataPost
                            )
                        } else {
                            if (detailPost.filePosts.isNotEmpty()) {
                                optionsMenuMyDetailPost(
                                    view,
                                    detailPost.idPost,
                                    detailPost.postDesc,
                                    detailPost.user.fullname,
                                    detailPost.filePosts.first().url,
                                    dataPost
                                )
                            } else {
                                optionsMenuMyDetailPost(
                                    view,
                                    detailPost.idPost,
                                    detailPost.postDesc,
                                    detailPost.user.fullname,
                                    "",
                                    dataPost
                                )
                            }
                        }
                    } else {
                        if (getSimpler != null) {
                            optionsMenuDetailSimplerPostOtherUser(
                                view,
                                detailPost.idPost
                            )
                        } else {
                            if (detailPost.filePosts.isNotEmpty()) {
                                optionsMenuDetailPostOtherUser(
                                    view,
                                    detailPost.idPost,
                                    detailPost.postDesc,
                                    detailPost.user.fullname,
                                    detailPost.filePosts.first().url
                                )
                            } else {
                                optionsMenuDetailPostOtherUser(
                                    view,
                                    detailPost.idPost,
                                    detailPost.postDesc,
                                    detailPost.user.fullname,
                                    ""
                                )
                            }
                        }
                    }
                }
                if (detailPost.user.id != userId) {
                    binding.constraintUser.setOnClickListener {
                        val i = Intent(this, ProfileOtherActivity::class.java)
                        i.putExtra("getIdUser", detailPost.user.id.toLong())
                        startActivity(i)
                    }
                }
                if (detailPost.user.profilePic.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(R.drawable.img_user)
                        .into(binding.imgPeople)
                } else {
                    Glide.with(this)
                        .load(detailPost.user.profilePic)
                        .into(binding.imgPeople)
                }
                binding.txtFullName.text = detailPost.user.fullname
                if (detailPost.user.status == "Verified" ||
                    detailPost.user.status == "Official") {
                    binding.icVerified.visibility = View.VISIBLE
                }
                binding.txtPeopleInterest.text = detailPost.user.passion
                binding.txtPostTime.text = detailPost.createdDate.toTimeAgo()
                if (detailPost.postStatus) {
                    binding.icPublicPost.setImageResource(R.drawable.ic_outlined_simpler)
                }
                val imageSlider = binding.imgSlider
                var isVideo = false
                val imageList = ArrayList<SlideModel>().apply {
                    detailPost.filePosts.map { file ->
                        add(SlideModel(file.url))
                        if (file.url.split(".").last() == "mp4") isVideo = true
                    }
                }
                if (imageList.isNotEmpty()) {
                    if (imageList.size == 1 && isVideo) {
                        imageSlider.visibility = View.INVISIBLE
                        binding.videoPreview.visibility = View.VISIBLE
                        Glide.with(this)
                            .load(imageList[0].imageUrl)
                            .into(binding.videoPreview)
                        binding.videoPreview.setOnClickListener {
                            val i = Intent(this, VideoActivity::class.java)
                            i.putExtra(VideoActivity.INTENT_VIDEO_URL, detailPost.filePosts[0].url)
                            startActivity(i)
                        }
                    } else {
                        binding.videoPreview.visibility = View.INVISIBLE
                        imageSlider.visibility = View.VISIBLE
                        imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
                    }
                } else {
                    imageSlider.visibility = View.GONE
                    binding.videoPreview.visibility = View.GONE
                }
                binding.txtDescription.text = detailPost.postDesc
                binding.chipPostTopicsFirst.text = detailPost.postTags.first()
                when (detailPost.postTags.size) {
                    2 -> {
                        binding.chipPostTopicsSecond.text = detailPost.postTags[1]
                        binding.chipPostTopicsSecond.visibility = View.VISIBLE
                    }
                    3 -> {
                        binding.chipPostTopicsSecond.text = detailPost.postTags[1]
                        binding.chipPostTopicsSecond.visibility = View.VISIBLE
                        binding.chipPostTopicsThird.text = detailPost.postTags[2]
                        binding.chipPostTopicsThird.visibility = View.VISIBLE
                    }
                    4 -> {
                        binding.chipPostTopicsSecond.text = detailPost.postTags[1]
                        binding.chipPostTopicsSecond.visibility = View.VISIBLE
                        binding.chipPostTopicsThird.text = detailPost.postTags[2]
                        binding.chipPostTopicsThird.visibility = View.VISIBLE
                        binding.chipPostTopicsFourth.text = detailPost.postTags[3]
                        binding.chipPostTopicsFourth.visibility = View.VISIBLE
                    }
                    5 -> {
                        binding.chipPostTopicsSecond.text = detailPost.postTags[1]
                        binding.chipPostTopicsSecond.visibility = View.VISIBLE
                        binding.chipPostTopicsThird.text = detailPost.postTags[2]
                        binding.chipPostTopicsThird.visibility = View.VISIBLE
                        binding.chipPostTopicsFourth.text = detailPost.postTags[3]
                        binding.chipPostTopicsFourth.visibility = View.VISIBLE
                        binding.chipPostTopicsFifth.text = detailPost.postTags.last()
                        binding.chipPostTopicsFifth.visibility = View.VISIBLE
                    }
                }
                if (detailPost.liked) {
                    binding.icSolidLove.visibility = View.VISIBLE
                    binding.icLove.visibility = View.INVISIBLE
                } else {
                    binding.icLove.visibility = View.VISIBLE
                    binding.icSolidLove.visibility = View.INVISIBLE
                }
                binding.txtTotalLove.text = detailPost.totalLike.toString()
                binding.icLove.setOnClickListener {
                    likePost(detailPost.idPost)
                }
                binding.icSolidLove.setOnClickListener {
                    unLikePost(detailPost.idPost)
                }
                binding.icComment.isEnabled = false
                binding.txtTotalComment.text = detailPost.totalKomentar.toString()
                if (detailPost.bookmarked) {
                    binding.icSolidSavePost.visibility = View.VISIBLE
                    binding.icSavePost.visibility = View.INVISIBLE
                } else {
                    binding.icSavePost.visibility = View.VISIBLE
                    binding.icSolidSavePost.visibility = View.INVISIBLE
                }
                binding.icSavePost.setOnClickListener {
                    insertBookmark(detailPost.idPost)
                }
                binding.icSolidSavePost.setOnClickListener {
                    deleteBookmark(detailPost.idPost)
                }
                binding.recyclerComment.apply {
                    layoutManager = LinearLayoutManager(
                        this@DetailPostActivity,
                        RecyclerView.VERTICAL,
                        true
                    )
                    adapter = CommentAdapter(
                        it.data.komentarPost,
                        onItemClick = { profile ->
                            val i = Intent(this@DetailPostActivity, ProfileOtherActivity::class.java)
                            i.putExtra("getIdUser", profile.idUser.id.toLong())
                            startActivity(i)
                        },
                        object : CommentAdapter.OptionsMenuClickListener {
                        override fun onOptionsMenuOtherUserClicked(view: View, idComment: Int) {
                            optionsMenuCommentOtherUser(view, idComment)
                        }

                        override fun onOptionsMenuUserClicked(view: View, idComment: Int, position: Int) {
                            optionsMenuCommentUser(view, idComment, position)
                        }
                    })
                }
            } else {
                showMessageToast(it.message)
            }
        }

        viewModel.postDeletePostResponse.observe(this) {
            if (it.status == "200") {
                onBackPressed()
            } else {
                showMessageToast(it.message)
            }
        }

        viewModel.postLikePostResponse.observe(this) {
            if (it.status == "200") {
                val getIdPost = intent.getIntExtra("idPost", 0)
                getDetailPost(getIdPost)
                binding.icLove.visibility = View.INVISIBLE
                binding.icSolidLove.visibility = View.VISIBLE
            } else {
                showMessageToast(it.message)
            }
        }

        viewModel.postUnlikePostResponse.observe(this) {
            if (it.status == "200") {
                val getIdPost = intent.getIntExtra("idPost", 0)
                getDetailPost(getIdPost)
                binding.icSolidLove.visibility = View.INVISIBLE
                binding.icLove.visibility = View.VISIBLE
            } else {
                showMessageToast(it.message)
            }
        }

        viewModel.postBookmarkPostResponse.observe(this) {
            if (it.status == "200") {
                val getIdPost = intent.getIntExtra("idPost", 0)
                getDetailPost(getIdPost)
                binding.icSavePost.visibility = View.INVISIBLE
                binding.icSolidSavePost.visibility = View.VISIBLE
            } else {
                showMessageToast(it.message)
            }
        }

        viewModel.deleteBookmarkPostResponse.observe(this) {
            if (it.status == "200") {
                val getIdPost = intent.getIntExtra("idPost", 0)
                getDetailPost(getIdPost)
                binding.icSolidSavePost.visibility = View.INVISIBLE
                binding.icSavePost.visibility = View.VISIBLE
            } else {
                showMessageToast(it.message)
            }
        }

        viewModel.postCommentResponse.observe(this) {
            if (it.status == "200") {
                val getIdPost = intent.getIntExtra("idPost", 0)
                getDetailPost(getIdPost)
                (binding.recyclerComment.adapter as CommentAdapter?)?.apply {
                    notifyDataSetChanged()
                }
            } else {
                showMessageToast(it.message)
            }
        }

        viewModel.deleteCommentResponse.observe(this) {
            if (it.first.status == "200") {
                val getIdPost = intent.getIntExtra("idPost", 0)
                getDetailPost(getIdPost)
                (binding.recyclerComment.adapter as CommentAdapter?)?.apply {
                    var findAt = -1
                    allResponse.forEachIndexed { index, _ ->
                        if (index == it.second) {
                            findAt = index
                        }
                    }
                    if (findAt != -1) {
                        allResponse.removeAt(findAt)
                        notifyItemRemoved(it.second)
                    }
                }
            } else {
                showMessageToast(it.first.message)
            }
        }

        val loadingUI = CustomLoadingDialog(this)
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) loadingUI.show() else loadingUI.dismiss()
        }
    }

    private fun getDetailPost(idPost: Int) {
        lifecycleScope.launch {
            viewModel.getDetailPostFromServer(idPost)
        }
        binding.editComment.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.txtSend.isEnabled = true
                binding.txtSend.setOnClickListener {
                    binding.editComment.text = null
                    insertComment(idPost, text.toString())
                }
            } else {
                binding.txtSend.isEnabled = false
            }
        }
    }

    private fun likePost(idPost: Int) {
        lifecycleScope.launch {
            viewModel.postLikePostToServer(idPost)
        }
    }

    private fun unLikePost(idPost: Int) {
        lifecycleScope.launch {
            viewModel.postUnlikePostToServer(idPost)
        }
    }

    fun deletePost(idPost: Int) {
        lifecycleScope.launch {
            viewModel.postDeletePostToServer(idPost)
        }
    }

    private fun insertBookmark(idPost: Int) {
        lifecycleScope.launch {
            viewModel.postBookmarkPostToServer(idPost)
        }
    }

    private fun deleteBookmark(idPost: Int) {
        lifecycleScope.launch {
            viewModel.deleteBookmarkPostToServer(idPost)
        }
    }

    private fun insertComment(idPost: Int, fieldComment: String) {
        lifecycleScope.launch {
            viewModel.postCommentToServer(idPost, fieldComment)
        }
    }

    private fun deleteComment(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.deleteCommentToServer(idPost, position)
        }
    }

    private fun optionsMenuMyDetailPost(
        view: View,
        idPost: Int,
        text: String,
        title: String,
        image: String?,
        dataPost: EditPostResponse
    ) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_my_post)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_edit -> {
                    val i = Intent(this, EditPostActivity::class.java)
                    i.putExtra("getData", dataPost)
                    startActivity(i)
                }
                R.id.menu_delete -> {
                    DeletePostAtDetailPostDialog().apply {
                        val bundle = Bundle().apply { putString("getId", "$idPost") }
                        arguments = bundle
                    }.show(supportFragmentManager, "DeletePostAtDetailPost")
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

    private fun optionsMenuDetailPostOtherUser(
        view: View,
        idPost: Int,
        text: String,
        title: String,
        image: String?,
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

    private fun optionsMenuMyDetailSimplerPost(
        view: View,
        idPost: Int,
        dataPost: EditPostResponse
    ) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_my_detail_post)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_edit -> {
                    val getSimpler = intent.getStringExtra("simpler")
                    val i = Intent(this, EditPostActivity::class.java)
                    i.putExtra("simpler", getSimpler)
                    i.putExtra("getData", dataPost)
                    startActivity(i)
                }
                R.id.menu_delete -> {
                    DeletePostAtDetailPostDialog().apply {
                        val bundle = Bundle().apply { putString("getId", "$idPost") }
                        arguments = bundle
                    }.show(supportFragmentManager, "DeletePostAtDetailPost")
                }
            }
            false
        }

        // Show icons on popup menu
        showIconOnPopupMenu(popupMenu)
        popupMenu.show()
    }

    private fun optionsMenuDetailSimplerPostOtherUser(
        view: View,
        idPost: Int
    ) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_detail_post_other_people)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_report -> {
                    val i = Intent(this, ReportActivity::class.java)
                    i.putExtra("getIdPost", idPost)
                    startActivity(i)
                }
            }
            false
        }

        // Show icons on popup menu
        showIconOnPopupMenu(popupMenu)
        popupMenu.show()
    }

    private fun optionsMenuCommentOtherUser(view: View, idComment: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_detail_post_other_people)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_report -> {
                    val i = Intent(this, ReportActivity::class.java)
                    i.putExtra("getIdComment", idComment)
                    startActivity(i)
                }
            }
            false
        }

        // Show icons on popup menu
        showIconOnPopupMenu(popupMenu)
        popupMenu.show()
    }

    private fun optionsMenuCommentUser(view: View, idComment: Int, position: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_my_comment)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_delete -> {
                    deleteComment(idComment, position)
                }
            }
            false
        }

        // Show icons on popup menu
        showIconOnPopupMenu(popupMenu)
        popupMenu.show()
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