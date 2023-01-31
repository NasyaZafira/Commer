package com.commer.app.ui.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.commer.app.R
import com.commer.app.databinding.DeletePostDialogBinding
import com.commer.app.ui.main.MainActivity

class DeletePostProfileDialog : DialogFragment() {

    private var _binding: DeletePostDialogBinding? = null
    private val binding get() = _binding!!
    private var getId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getId = arguments?.getString("getId") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)
        _binding = DeletePostDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDelete.setOnClickListener {
            val result = getId.split(",")
            (requireActivity() as MainActivity?)?.deletePostAtProfile(result.first().toInt(), result.last().toInt())
            dialog?.dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val window: Window = dialog!!.window!!
        val wlp: WindowManager.LayoutParams = window.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}