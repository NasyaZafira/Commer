package com.commer.app.ui.post.newpost

import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.fragment.app.DialogFragment
import com.commer.app.databinding.AlertSelectedTopicsDialogBinding

class AlertTopicsDialog : DialogFragment() {

    private var _binding: AlertSelectedTopicsDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlertSelectedTopicsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val closeTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }
            override fun onFinish() {
                dialog?.dismiss()
            }
        }
        closeTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val window: Window = dialog!!.window!!
        val wlp: WindowManager.LayoutParams = window.attributes

        wlp.gravity = Gravity.TOP
        wlp.verticalMargin = 0.13F
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}