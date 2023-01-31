package com.commer.app.ui.settings.bookmarks

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.model.remote.post.editpost.EditPostResponse
import com.commer.app.databinding.ActivityBookmarksBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.homepage.NotifSavedDialog
import com.commer.app.ui.homepage.VideoActivity
import com.commer.app.ui.post.detail.DetailPostActivity
import com.commer.app.ui.post.editpost.EditPostActivity
import com.commer.app.ui.profile.other.ProfileOtherActivity
import com.commer.app.ui.report.ReportActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Method

@AndroidEntryPoint
class BookmarksActivity : BaseActivity() {

    private lateinit var binding: ActivityBookmarksBinding
    private val viewModel: BookmarksViewModel by viewModels()
    private val bookmarksAdapter = BookmarksAdapter(
        mutableListOf(),
        onItemClick = { item ->
            val i = Intent(this@BookmarksActivity, DetailPostActivity::class.java)
            i.putExtra("idPost", item.idPost)
            startActivity(i)
        },
        onVideoClick = { video ->
            val i = Intent(this@BookmarksActivity, VideoActivity::class.java)
            i.putExtra(VideoActivity.INTENT_VIDEO_URL, video.filePosts[0].url)
            startActivity(i)
        },
        onProfileClick = { profile ->
            val i = Intent(this@BookmarksActivity, ProfileOtherActivity::class.java)
            i.putExtra("getIdUser", profile.user.id.toLong())
            startActivity(i)
        },
        object : BookmarksAdapter.OnClickedAtBookmarks {
            override fun onOptionsMenuOtherUserClicked(
                view: View,
                idPost: Int,
                text: String,
                title: String,
                image: String?
            ) {
                optionsMenuPostOtherUser(view, idPost, text, title, image)
            }

            override fun onOptionsMenuUserClicked(
                view: View,
                idPost: Int,
                position: Int,
                text: String,
                title: String,
                image: String?,
                dataPost: EditPostResponse
            ) {
                optionsMenuMyPost(view, idPost, position, text, title, image, dataPost)
            }

            override fun onOptionsMenuSimplerOtherUserClicked(
                view: View,
                idPost: Int
            ) {
                optionsMenuSimplerPostOtherUser(view, idPost)
            }

            override fun onOptionsMenuSimplerUserClicked(
                view: View,
                idPost: Int,
                position: Int,
                dataPost: EditPostResponse
            ) {
                optionsMenuMySimplerPost(view, idPost, position, dataPost)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityBookmarksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        getBookmarksFromServer()

        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefresh.setOnRefreshListener {
            getBookmarksFromServer()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun setupObserver() {
        viewModel.getBookmarksResponse.observe(this) {
            if (it.status == "200") {
                if (it.data.isNotEmpty()) {
                    binding.imgHaveNoBookmarks.visibility = View.GONE
                    binding.recyclerBookmarks.visibility = View.VISIBLE
                } else {
                    binding.imgHaveNoBookmarks.visibility = View.VISIBLE
                }
                binding.recyclerBookmarks.apply {
                    layoutManager = LinearLayoutManager(
                        this@BookmarksActivity,
                        RecyclerView.VERTICAL,
                        true
                    )
                    bookmarksAdapter.postResponse.clear()
                    bookmarksAdapter.postResponse.addAll(it.data)
                    adapter = bookmarksAdapter
                }
            }
        }
        viewModel.postLikePostResponse.observe(this) {
            if (it.first.status == "200") {
                (binding.recyclerBookmarks.adapter as BookmarksAdapter?)?.apply {
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
                (binding.recyclerBookmarks.adapter as BookmarksAdapter?)?.apply {
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
                getBookmarksFromServer()
                (binding.recyclerBookmarks.adapter as BookmarksAdapter?)?.apply {
                    var findAt = -1
                    postResponse.forEachIndexed { index, _ ->
                        if (index == it.second) {
                            findAt = index
                        }
                    }
                    if (findAt != -1) {
                        postResponse.removeAt(findAt)
                        notifyItemRemoved(it.second)
                    }
                    if (postResponse.size == 0) {
                        binding.imgHaveNoBookmarks.visibility = View.VISIBLE
                        binding.recyclerBookmarks.visibility = View.GONE
                    }
                }
            } else {
                showMessageToast(it.first.message)
            }
        }
        viewModel.postBookmarkPostResponse.observe(this) {
            if (it.first.status == "200") {
                (binding.recyclerBookmarks.adapter as BookmarksAdapter?)?.apply {
                    postResponse[it.second].bookmarked = true
                    notifyItemChanged(it.second)
                }
            } else {
                showMessageToast(it.first.message)
            }
        }
        viewModel.deleteBookmarkPostResponse.observe(this) {
            if (it.first.status == "200") {
                (binding.recyclerBookmarks.adapter as BookmarksAdapter?)?.apply {
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

    private fun getBookmarksFromServer() {
        lifecycleScope.launch {
            viewModel.getBookmarksFromServer()
        }
    }

    fun deletePost(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.postDeletePostToServer(idPost, position)
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
            NotifSavedDialog().show(supportFragmentManager, "NotifySavedDialog")
        }
    }

    private fun deleteBookmarkPost(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.deleteBookmarkPostToServer(idPost, position)
            NotifyRemoveBookmarkDialog().show(supportFragmentManager, "NotifyRemoveBookmarkDialog")
        }
    }

    private fun optionsMenuMyPost(
        view: View,
        idPost: Int,
        position: Int,
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
                    DeletePostAtBookmarksDialog().apply {
                        val bundle = Bundle().apply { putString("getId", "$idPost,$position") }
                        arguments = bundle
                    }.show(supportFragmentManager, "DeletePostAtBookmarksDialog")
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

    private fun optionsMenuPostOtherUser(
        view: View, idPost: Int, text: String, title: String, image: String?
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

    private fun optionsMenuMySimplerPost(view: View, idPost: Int, position: Int, dataPost: EditPostResponse) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_my_detail_post)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_edit -> {
                    val i = Intent(this, EditPostActivity::class.java)
                    i.putExtra("getData", dataPost)
                    startActivity(i)
                }
                R.id.menu_delete -> {
                    DeletePostAtBookmarksDialog().apply {
                        val bundle = Bundle().apply { putString("getId", "$idPost,$position") }
                        arguments = bundle
                    }.show(supportFragmentManager, "DeletePostAtBookmarksDialog")
                }
            }
            false
        }

        // Show icons on popup menu
        showIconOnPopupMenu(popupMenu)
        popupMenu.show()
    }

    private fun optionsMenuSimplerPostOtherUser(view: View, idPost: Int) {
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