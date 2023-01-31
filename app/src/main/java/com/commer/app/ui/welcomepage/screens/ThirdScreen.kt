package com.commer.app.ui.welcomepage.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.commer.app.databinding.FragmentThirdScreenBinding
import com.commer.app.ui.welcomepage.WelcomeActivity

class ThirdScreen : Fragment() {

    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoToLogin.setOnClickListener {
            val activity = requireActivity() as WelcomeActivity
            activity.moveToLogin()
        }

        binding.btnGoToSignup.setOnClickListener {
            val activity = requireActivity() as WelcomeActivity
            activity.moveToSignUp()
        }
    }
}