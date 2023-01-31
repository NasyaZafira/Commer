package com.commer.app.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commer.app.R
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.post.editpost.EditPostResponse
import com.commer.app.databinding.FragmentProfileBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.CustomStringFormat.toGlobalMoney
import com.commer.app.ui.homepage.VideoActivity
import com.commer.app.ui.post.detail.DetailPostActivity
import com.commer.app.ui.profile.followers.FollowersActivity
import com.commer.app.ui.profile.following.FollowingActivity
import com.commer.app.ui.post.editpost.EditPostActivity
import com.commer.app.ui.report.ReportActivity
import com.commer.app.ui.settings.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.lang.reflect.Method

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var loadingUI: CustomLoadingDialog
    private val getIdUser = CommerSharedPref.userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.detailUserFromServer(getIdUser!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary_color)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingUI = CustomLoadingDialog(requireContext())

        val nav = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        nav?.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.navigation_profile) {
                binding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
            }
        }

        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefresh.setOnRefreshListener {
            getDetailUser(getIdUser!!)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.postLikePostResponse.removeObservers(viewLifecycleOwner)
        viewModel.postUnlikePostResponse.removeObservers(viewLifecycleOwner)
        viewModel.loading.removeObservers(viewLifecycleOwner)
        viewModel.message.removeObservers(viewLifecycleOwner)
    }

    private fun setupObserver() {
        viewModel.detailUserResponse.observe(viewLifecycleOwner) {
            val response = it.data
            if (it.status == "200") {
                binding.imgBtnSettings.setOnClickListener {
                    val i = Intent(requireContext(), SettingsActivity::class.java)
                    i.putExtra("getData", response.detailProfile)
                    startActivity(i)
                }
                binding.linearTxtFollowing.setOnClickListener {
                    val i = Intent(requireContext(), FollowingActivity::class.java)
                    i.putExtra("getIdUser", response.detailProfile.id)
                    startActivity(i)
                }
                binding.linearTxtFollowers.setOnClickListener {
                    val i = Intent(requireContext(), FollowersActivity::class.java)
                    i.putExtra("getIdUser", response.detailProfile.id)
                    startActivity(i)
                }
                if (response.detailProfile.profilePic.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(R.drawable.img_user)
                        .into(binding.imgProfile)
                } else {
                    Glide.with(requireContext())
                        .load(response.detailProfile.profilePic)
                        .into(binding.imgProfile)
                }
                if (response.detailProfile.status == "Verified" ||
                    response.detailProfile.status == "Official") {
                    binding.icVerified.visibility = View.VISIBLE
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
                binding.txtTotalFollowers.text = response.detailProfile.totalFollowers.toGlobalMoney()
                binding.txtTotalFollowing.text = response.detailProfile.totalFollowing.toGlobalMoney()
                if (response.postUser.isEmpty()) {
                    binding.recyclerPostAtProfile.visibility = View.INVISIBLE
                } else {
                    binding.recyclerPostAtProfile.apply {
                        layoutManager = LinearLayoutManager(
                            context,
                            RecyclerView.VERTICAL,
                            true
                        )
                        adapter = ProfilePostAdapter(
                            response.postUser,
                            onVideoClick = { video ->
                                val i = Intent(requireContext(), VideoActivity::class.java)
                                i.putExtra(VideoActivity.INTENT_VIDEO_URL, video.filePosts[0].url)
                                startActivity(i)
                            },
                            onItemClick = { item ->
                                val i = Intent(requireContext(), DetailPostActivity::class.java)
                                i.putExtra("idPost", item.idPost)
                                startActivity(i)
                            },
                            object : ProfilePostAdapter.OnClickedAtProfile {
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
        viewModel.postLikePostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPostAtProfile.adapter as ProfilePostAdapter?)?.apply {
                    postResponse[it.second].totalLike++
                    postResponse[it.second].liked = true
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postUnlikePostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPostAtProfile.adapter as ProfilePostAdapter?)?.apply {
                    postResponse[it.second].totalLike--
                    postResponse[it.second].liked = false
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postDeletePostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPostAtProfile.adapter as ProfilePostAdapter?)?.apply {
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
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postBookmarkPostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPostAtProfile.adapter as ProfilePostAdapter?)?.apply {
                    postResponse[it.second].bookmarked = true
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.deleteBookmarkPostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPostAtProfile.adapter as ProfilePostAdapter?)?.apply {
                    postResponse[it.second].bookmarked = false
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) loadingUI.show() else loadingUI.dismiss()
        }
    }

    private fun getDetailUser(idUser: Int) {
        lifecycleScope.launch {
            viewModel.detailUserFromServer(idUser)
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
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.menu_my_post)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_edit -> {
                    val i = Intent(requireContext(), EditPostActivity::class.java)
                    i.putExtra("getData", dataPost)
                    startActivity(i)
                }
                R.id.menu_delete -> {
                    DeletePostProfileDialog().apply {
                        val bundle = Bundle().apply { putString("getId", "$idPost,$position") }
                        arguments = bundle
                    }.show(parentFragmentManager, "DeletePostProfileDialog")
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

    private fun optionsMenuPostOtherUser(view: View, idPost: Int, text: String, title: String, image: String?) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.menu_post_other_people)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_report -> {
                    val i = Intent(requireContext(), ReportActivity::class.java)
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