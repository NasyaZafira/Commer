package com.commer.app.ui.post.newpost

import android.os.Bundle
import android.view.*
import com.commer.app.databinding.BottomSheetPostStatusBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetPostStatus : BottomSheetDialogFragment() {
    private var _binding: BottomSheetPostStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetPostStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cardPublicPost = binding.cardPublicPost
        val cardSimplerPost = binding.cardSimplerPost
        cardPublicPost.setOnClickListener {
            cardPublicPost.isChecked = true
            cardSimplerPost.isChecked = false
            getValueCard(false)
        }
        cardSimplerPost.setOnClickListener {
            cardSimplerPost.isChecked = true
            cardPublicPost.isChecked = false
            getValueCard(true)
        }
    }

    private fun getValueCard(status: Boolean?) {
        binding.btnPost.isEnabled = status != null
        binding.btnPost.setOnClickListener {
            (requireActivity() as NewPostActivity?)?.showPostStatus(status!!)
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val window: Window = dialog!!.window!!
        val wlp: WindowManager.LayoutParams = window.attributes

        wlp.gravity = Gravity.BOTTOM
        window.attributes = wlp
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}