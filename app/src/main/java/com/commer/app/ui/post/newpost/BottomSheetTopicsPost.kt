package com.commer.app.ui.post.newpost

import android.os.Bundle
import android.view.*
import androidx.core.view.children
import com.commer.app.databinding.BottomSheetTopicsPostBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class BottomSheetTopicsPost : BottomSheetDialogFragment() {

    private var _binding: BottomSheetTopicsPostBinding? = null
    private val binding get() = _binding!!
    private var tags: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tags = arguments?.getString("tags") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTopicsPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrTags = tags.split(",")
        binding.chipTagPost.children.map { it as Chip }.forEach {
            var alreadyChecked = false
            arrTags.forEach { tag ->
                if (tag == it.text.toString() && !alreadyChecked) alreadyChecked = true
            }
            it.isChecked = alreadyChecked
            it.setOnCheckedChangeListener { _, _ ->
                combineInterest()
            }
        }
    }

    private fun combineInterest() {
        var interest = ""
        binding.chipTagPost.children.map { it as Chip }.forEach {
            if (it.isChecked) {
                if (interest.isEmpty()) interest = it.text.toString()
                else interest += ",${it.text}"
            }
        }
        (requireActivity() as NewPostActivity?)?.showSelectedTags(interest)
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