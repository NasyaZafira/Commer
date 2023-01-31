package com.commer.app.ui.simplerpost

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.R
import com.commer.app.data.model.remote.post.editpost.EditPostResponse
import com.commer.app.databinding.FragmentOABinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.homepage.VideoActivity
import com.commer.app.ui.post.detail.DetailPostActivity
import com.commer.app.ui.post.editpost.EditPostActivity
import com.commer.app.ui.profile.DeletePostProfileDialog
import com.commer.app.ui.profile.other.ProfileOtherActivity
import com.commer.app.ui.report.ReportActivity
import com.commer.app.ui.simplerpost.official.BottomSheetFilterOfficial
import com.commer.app.ui.simplerpost.official.OfficialPostAdapter
import com.commer.app.ui.simplerpost.official.OfficialPostViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.lang.reflect.Method

class OAFragment : Fragment() {

    private var _binding: FragmentOABinding? = null
    private val binding get() = _binding!!
    private val viewModel: OfficialPostViewModel by viewModels({requireParentFragment()})
    private lateinit var loadingUI: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.getOfficialPostFromServer(null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOABinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingUI = CustomLoadingDialog(requireContext())

        val nav = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        nav?.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.navigation_simpler) {
                binding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
            }
        }

        binding.btnFilter.setOnClickListener {
            BottomSheetFilterOfficial().show(parentFragmentManager, "FilterOfficialPost")
        }

        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getOfficialPostFromServer(null)
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObserver() {
        viewModel.officialPostResponse.observe(viewLifecycleOwner) {
            if (it.status == "200") {
                binding.recyclerOfficialPost.apply {
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        RecyclerView.VERTICAL,
                        true
                    )
                    adapter = OfficialPostAdapter(
                        it.data,
                        onItemClick = { item ->
                            val i = Intent(requireContext(), DetailPostActivity::class.java)
                            i.putExtra("simpler", "simpler")
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
                        },
                        object : OfficialPostAdapter.OnClickedAtOfficialPost {
                            override fun onOptionsMenuOtherUserClicked(view: View, idPost: Int) {
                                optionsMenuPostOtherUser(view, idPost)
                            }

                            override fun onOptionsMenuUserClicked(
                                view: View,
                                idPost: Int,
                                position: Int,
                                dataPost: EditPostResponse
                            ) {
                                optionsMenuMyPost(view, idPost, position, dataPost)
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
        viewModel.postLikePostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerOfficialPost.adapter as OfficialPostAdapter?)?.apply {
                    officialPost[it.second].totalLike++
                    officialPost[it.second].liked = true
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postUnlikePostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerOfficialPost.adapter as OfficialPostAdapter?)?.apply {
                    officialPost[it.second].totalLike--
                    officialPost[it.second].liked = false
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postDeletePostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerOfficialPost.adapter as OfficialPostAdapter?)?.apply {
                    var findAt = -1
                    officialPost.forEachIndexed { index, _ ->
                        if (index == it.second) {
                            findAt = index
                        }
                    }
                    if (findAt != -1) {
                        officialPost.removeAt(findAt)
                        notifyItemRemoved(it.second)
                    }
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.postBookmarkPostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerOfficialPost.adapter as OfficialPostAdapter?)?.apply {
                    officialPost[it.second].bookmarked = true
                    notifyItemChanged(it.second)
                }
            } else {
                Toast.makeText(requireContext(), it.first.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.deleteBookmarkPostResponse.observe(viewLifecycleOwner) {
            if (it.first.status == "200") {
                (binding.recyclerOfficialPost.adapter as OfficialPostAdapter?)?.apply {
                    officialPost[it.second].bookmarked = false
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

    private fun optionsMenuMyPost(
        view: View,
        idPost: Int,
        position: Int,
        dataPost: EditPostResponse
    ) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.menu_my_detail_post)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_edit -> {
                    val i = Intent(requireContext(), EditPostActivity::class.java)
                    i.putExtra("simpler", "simpler")
                    i.putExtra("getData", dataPost)
                    startActivity(i)
                }
                R.id.menu_delete -> {
                    DeletePostProfileDialog().apply {
                        val bundle = Bundle().apply { putString("getId", "$idPost,$position") }
                        arguments = bundle
                    }.show(parentFragmentManager, "DeletePostProfileDialog")
                }
            }
            false
        }

        // Show icons on popup menu
        showIconOnPopupMenu(popupMenu)
        popupMenu.show()
    }

    private fun optionsMenuPostOtherUser(view: View, idPost: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.menu_detail_post_other_people)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_report -> {
                    val i = Intent(requireContext(), ReportActivity::class.java)
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