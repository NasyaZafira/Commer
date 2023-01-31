package com.commer.app.ui.welcomepage.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.commer.app.databinding.FragmentFirstScreenBinding
import com.commer.app.ui.CheckInternetConnection
import com.commer.app.ui.welcomepage.WelcomeActivity

class FirstScreen : Fragment() {

    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!
    private val checkInternet = CheckInternetConnection()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNextWelcomePage.setOnClickListener {
            val activity = requireActivity() as WelcomeActivity
            activity.moveToSecondScreen()
        }

        binding.btnSkipWelcomePage.setOnClickListener {
            val activity = requireActivity() as WelcomeActivity
            activity.moveToThirdScreen()
        }
    }

    override fun onStart() {
        super.onStart()
        checkInternet.isNetworkAvailable(requireContext(), parentFragmentManager)
    }
}