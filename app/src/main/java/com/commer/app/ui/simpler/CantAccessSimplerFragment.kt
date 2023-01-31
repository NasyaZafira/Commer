package com.commer.app.ui.simpler

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.databinding.FragmentCantAccessSimplerBinding
import com.commer.app.ui.CustomLoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@AndroidEntryPoint
class CantAccessSimplerFragment : Fragment() {

    private var _binding: FragmentCantAccessSimplerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SimplerPaymentViewModel by viewModels()
    private lateinit var loadingUI: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.getDetailUser()
            viewModel.getPaymentStatusFromServer()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCantAccessSimplerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingUI = CustomLoadingDialog(requireContext())
        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getDetailUser()
                viewModel.getPaymentStatusFromServer()
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObserver() {
        viewModel.getPaymentStatusResponse.observe(viewLifecycleOwner) { response ->
            binding.btnGetYourPlan.setOnClickListener {
                when (response.message) {
                    "no transaction" -> {
                        val i = Intent(activity, SubscriptionPlanActivity::class.java)
                        startActivity(i)
                    }
                    "On Progress" -> {
                        val i = Intent(activity, SimplerPaymentLoadingActivity::class.java)
                        startActivity(i)
                    }
                    "Failed" -> {
                        val i = Intent(activity, SimplerPaymentFailedActivity::class.java)
                        startActivity(i)
                    }
                    "success" -> {
                        val userStatus = CommerSharedPref.userStatus
                        if (userStatus == "User") {
                            val i = Intent(activity, SubscriptionPlanActivity::class.java)
                            startActivity(i)
                        } else {
                            val i = Intent(activity, SimplerPaymentSuccessActivity::class.java)
                            val responseDate = response.data[0].createdDate
                            val responseDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                            val parseDate = responseDateFormat.parse(responseDate)
                            val newDateFormat = SimpleDateFormat("MMMM dd yyyy,\nhh:mm aaa")
                            val finalDate = newDateFormat.format(parseDate!!)
                            i.putExtra("date", finalDate)
                            i.putExtra("price", response.data[0].totalPaid)
                            i.putExtra(
                                "plan details",
                                "Simpler " + response.data[0].plan + " months plan"
                            )
                            i.putExtra("for x month", "(For " + response.data[0].plan + " months)")
                            startActivity(i)
                        }
                    }
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "Error message: ${response.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) loadingUI.show()
            else loadingUI.dismiss()
        }
        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

}