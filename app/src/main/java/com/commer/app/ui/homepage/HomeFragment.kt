package com.commer.app.ui.homepage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.ui.post.detail.DetailPostActivity
import com.commer.app.R
import com.commer.app.data.model.remote.post.editpost.EditPostResponse
import com.commer.app.databinding.FragmentHomeBinding
import com.commer.app.ui.BottomSheetError
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.post.editpost.EditPostActivity
import com.commer.app.ui.post.newpost.NewPostActivity
import com.commer.app.ui.profile.other.ProfileOtherActivity
import com.commer.app.ui.report.ReportActivity
import com.commer.app.ui.search.SearchActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.lang.reflect.Method

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var loadingUI: CustomLoadingDialog
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val postAdapter = PostAdapter(mutableListOf(), object : PostAdapter.OptionsMenuClickListener {
        override fun onOptionsMenuClicked(
            view: View,
            idPost: Int,
            text: String,
            title: String,
            image: String?
        ) {
            performOptionsMenuClick(view, idPost, text, title, image)
        }

        override fun onOptionsMenuForUser(
            view: View,
            idPost: Int,
            position: Int,
            text: String,
            title: String,
            image: String?,
            dataPost: EditPostResponse
        ) {
            performOptionsMenuForUserClick(view, idPost, position, text, title, image, dataPost)
        }

        override fun onBookmarkClicked(idPost: Int, position: Int) {
            performButtonBookmark(idPost, position)
        }

        override fun onDeleteBookmarkClicked(idPost: Int, position: Int) {
            performButtonDeleteBookmark(idPost, position)
        }

        override fun onLikeClicked(idPost: Int, position: Int) {
            performButtonLike(idPost, position)
        }

        override fun onUnlikeClicked(idPost: Int, position: Int) {
            performButtonUnlike(idPost, position)
        }
    }, onItemClick = { item ->
        val i = Intent(requireContext(), DetailPostActivity::class.java)
        i.putExtra("idPost", item.idPost)
        startActivity(i)
    }, onVideoClick = { video ->
        val i = Intent(requireContext(), VideoActivity::class.java)
        i.putExtra(VideoActivity.INTENT_VIDEO_URL, video.filePosts[0].url)
        startActivity(i)
    }, onProfileClick = { profile ->
        val i = Intent(requireContext(), ProfileOtherActivity::class.java)
        i.putExtra("getIdUser", profile.user.id.toLong())
        startActivity(i)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.getSuggestedUserFromServer()
            viewModel.getPostFromServer(null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary_color)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingUI = CustomLoadingDialog(requireContext())

        val nav = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        nav?.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.navigation_home) {
                binding.nestedScrollAtHome.fullScroll(NestedScrollView.FOCUS_UP)
            }
        }

        binding.imgBtnClose.setOnClickListener {
            binding.constraintSuggestPeople.visibility = View.GONE
            binding.recyclerSuggestedPeople.visibility = View.GONE
        }

        binding.btnSearch.setOnClickListener {
            val i = Intent(requireContext(), SearchActivity::class.java)
            startActivity(i)
        }

        binding.btnFilter.setOnClickListener {
            BottomSheetFilter().show(parentFragmentManager, "BottomSheetFilter")
        }

        binding.btnAddPost.setOnClickListener {
            val i = Intent(requireContext(), NewPostActivity::class.java)
            startActivity(i)
        }

        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getSuggestedUserFromServer()
                viewModel.getPostFromServer(null)
            }
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
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it) BottomSheetError().show(parentFragmentManager, "ExceptionAtHomeFragment")
        }
        viewModel.statusCode.observe(viewLifecycleOwner) {
            if (it != 200) {
                binding.constraintFilter.visibility = View.VISIBLE
                binding.imgHaveNoPost.visibility = View.VISIBLE
                binding.recyclerPost.visibility = View.INVISIBLE
            }
        }
        viewModel.postResponse.observe(viewLifecycleOwner) { response ->
            when (response.data.isNotEmpty()) {
                true -> {
                    binding.constraintFilter.visibility = View.VISIBLE
                    binding.imgHaveNoPost.visibility = View.INVISIBLE
                    binding.recyclerPost.visibility = View.VISIBLE
                    binding.recyclerPost.apply {
                        layoutManager = LinearLayoutManager(
                            context,
                            RecyclerView.VERTICAL,
                            true
                        )
                        postAdapter.allResponse.clear()
                        postAdapter.allResponse.addAll(response.data)
                        adapter = postAdapter
                    }
                }
                false -> {
                    binding.constraintFilter.visibility = View.GONE
                    binding.imgHaveNoPost.visibility = View.VISIBLE
                    binding.recyclerPost.visibility = View.INVISIBLE
                }
            }
        }
        viewModel.getSuggestedUserResponse.observe(viewLifecycleOwner) {
            when (it.data.isNotEmpty()) {
                true -> {
                    binding.recyclerSuggestedPeople.visibility = View.VISIBLE
                    binding.constraintSuggestPeople.visibility = View.VISIBLE
                }
                false -> {
                    binding.recyclerSuggestedPeople.visibility = View.VISIBLE
                    binding.constraintSuggestPeople.visibility = View.GONE
                }
            }
            binding.recyclerSuggestedPeople.apply {
                layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.VERTICAL,
                    false
                )
                adapter = SuggestPeopleAdapter(
                    it.data,
                    onItemClick = { profile ->
                        val i = Intent(requireContext(), ProfileOtherActivity::class.java)
                        i.putExtra("getIdUser", profile.id.toLong())
                        startActivity(i)
                    },
                    object : SuggestPeopleAdapter.ButtonOnClickListener {
                        override fun setButtonFollow(userId: Int) {
                            performButtonFollow(userId)
                        }

                        override fun setButtonUnFollow(userId: Int) {
                            performButtonUnfollow(userId)
                        }
                    })
            }
        }
        viewModel.postFollowUserResponse.observe(viewLifecycleOwner) {
            if (it.status != "200") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postUnFollowUserResponse.observe(viewLifecycleOwner) {
            if (it.status != "200") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postLikePostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPost.adapter as PostAdapter?)?.apply {
                    allResponse[it.second].totalLike++
                    allResponse[it.second].liked = true
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postUnlikePostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPost.adapter as PostAdapter?)?.apply {
                    allResponse[it.second].totalLike--
                    allResponse[it.second].liked = false
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postDeletePostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPost.adapter as PostAdapter?)?.apply {
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
                    if (allResponse.size == 0) {
                        binding.constraintFilter.visibility = View.VISIBLE
                        binding.imgHaveNoPost.visibility = View.VISIBLE
                        binding.recyclerPost.visibility = View.GONE
                    }
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postBookmarkPostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPost.adapter as PostAdapter?)?.apply {
                    allResponse[it.second].bookmarked = true
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.deleteBookmarkPostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerPost.adapter as PostAdapter?)?.apply {
                    allResponse[it.second].bookmarked = false
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) loadingUI.show()
            else loadingUI.dismiss()
        }
        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun performOptionsMenuClick(view: View, idPost: Int, text: String, title: String, image: String?) {
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

    private fun performOptionsMenuForUserClick(
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
                    DeletePostAtHomeDialog().apply {
                        val bundle = Bundle().apply { putString("getId", "$idPost,$position") }
                        arguments = bundle
                    }.show(parentFragmentManager, "DeletePostAtHomeDialog")
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

    private fun performButtonBookmark(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.postBookmarkPostToServer(idPost, position)
            NotifSavedDialog().show(parentFragmentManager, "NotifySavedDialog")
        }
    }

    private fun performButtonDeleteBookmark(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.deleteBookmarkPostToServer(idPost, position)
        }
    }

    private fun performButtonFollow(userId: Int) {
        lifecycleScope.launch {
            viewModel.postFollowUserToServer(userId)
            viewModel.getSuggestedUserFromServer()
        }
    }

    private fun performButtonUnfollow(userId: Int) {
        lifecycleScope.launch {
            viewModel.postUnFollowUserToServer(userId)
            viewModel.getSuggestedUserFromServer()
        }
    }

    private fun performButtonLike(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.postLikePostToServer(idPost, position)
        }
    }

    private fun performButtonUnlike(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModel.postUnlikePostToServer(idPost, position)
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