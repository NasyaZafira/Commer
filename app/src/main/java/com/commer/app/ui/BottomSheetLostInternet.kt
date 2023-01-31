package com.commer.app.ui

import android.os.Bundle
import android.view.*
import com.commer.app.databinding.BottomSheetErrorBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetLostInternet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = "You lost internet\nconnection :("
        val desc = "Make sure you are connected to internet. Try it one more time!"
        binding.txtSomething.text = title
        binding.txtMoment.text = desc
        binding.btnOkay.setOnClickListener {
            dialog?.dismiss()
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