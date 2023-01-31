package com.commer.app.ui.post.editpost

import android.os.Bundle
import android.view.*
import com.commer.app.databinding.BottomSheetPostStatusBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class StatusPostAtEditPost : BottomSheetDialogFragment() {

    private var _binding: BottomSheetPostStatusBinding? = null
    private val binding get() = _binding!!
    private var status: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        status = arguments?.getBoolean("status") ?: false
    }

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
        if (status) {
            cardSimplerPost.isChecked = true
            cardPublicPost.isChecked = false
            getValueCard(true)
        } else {
            cardSimplerPost.isChecked = false
            cardPublicPost.isChecked = true
            getValueCard(false)
        }
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
        binding.btnPost.isEnabled = true
    }

    private fun getValueCard(postStatus: Boolean) {
        binding.btnPost.setOnClickListener {
            (requireActivity() as EditPostActivity?)?.showPostStatus(postStatus)
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