package com.commer.app.ui.homepage

import android.os.Bundle
import android.view.*
import androidx.core.view.children
import com.commer.app.databinding.BottomSheetFilterPostBinding
import com.commer.app.ui.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class BottomSheetFilter : BottomSheetDialogFragment() {

    private var _binding: BottomSheetFilterPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetFilterPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chipFilterPost.children.map { it as Chip }.forEach {
            it.setOnCheckedChangeListener { _, _ ->
                val chipText = it.text
                binding.btnFilter.isEnabled = it.isChecked && !chipText.isNullOrEmpty()

                binding.btnFilter.setOnClickListener {
                    (requireActivity() as MainActivity?)?.getPostFromFilter(chipText.toString())
                    dialog?.dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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