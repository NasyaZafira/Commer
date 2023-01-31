package com.commer.app.ui.simpler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.commer.app.R
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.databinding.FragmentSimplerBinding
import com.commer.app.ui.simplerpost.SimplerPostsFragment

class SimplerFragment : Fragment() {

    private var _binding: FragmentSimplerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimplerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notSubscribeSimpler = CantAccessSimplerFragment()
        val subscribeSimpler = SimplerPostsFragment()
        val transaction = childFragmentManager.beginTransaction()

        val statusUser = CommerSharedPref.userStatus.toString().trim()

        if (statusUser == "User") {
            transaction.add(R.id.setFragmentSimpler, notSubscribeSimpler)
        } else {
            transaction.add(R.id.setFragmentSimpler, subscribeSimpler)
        }

        transaction.commit()
    }

}