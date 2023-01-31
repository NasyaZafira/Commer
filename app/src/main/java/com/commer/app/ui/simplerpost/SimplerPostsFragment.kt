package com.commer.app.ui.simplerpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.commer.app.R
import com.commer.app.databinding.FragmentSimplerPostsBinding
import com.commer.app.ui.simplerpost.official.OfficialPostViewModel
import com.commer.app.ui.simplerpost.verified.VerifiedPostViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SimplerPostsFragment : Fragment() {

    private var _binding: FragmentSimplerPostsBinding? = null
    private val binding get() = _binding!!
    private val viewModelForVerified: VerifiedPostViewModel by viewModels()
    private val viewModelForOfficial: OfficialPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.secondary_color)
        _binding = FragmentSimplerPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.apply {
            this.adapter = SimplerPostAdapter(this@SimplerPostsFragment)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Official Account"
                else -> "Verified Account"
            }
        }.attach()
    }

    fun getVerifiedPostFromFilter(tags: String?) {
        lifecycleScope.launch {
            viewModelForVerified.getVerifiedPostFromServer(tags)
        }
    }

    fun getOfficialPostFromFilter(tags: String?) {
        lifecycleScope.launch {
            viewModelForOfficial.getOfficialPostFromServer(tags)
        }
    }

}