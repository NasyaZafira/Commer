package com.commer.app.ui.settings.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.R
import com.commer.app.data.model.remote.settings.transaction.Data
import com.commer.app.databinding.ItemTransactionHistoryBinding
import com.commer.app.ui.TimeAgo.toCustomDate

class TransactionHistoryAdapter(
    private val transactionResponse: List<Data>
) : RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTransactionHistoryBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemTransactionHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = transactionResponse[position]
        val textTitle = "Simpler ${response.plan} months plan"
        val errorColor = ContextCompat.getColor(holder.itemView.context, R.color.error_color)
        val successColor = ContextCompat.getColor(holder.itemView.context, R.color.success_color)
        holder.binding.txtSimpler.text = textTitle
        holder.binding.txtDate.text = response.createdDate.toCustomDate()
        if (response.status == "Success") {
            holder.binding.txtStatusTransaction.text = response.status
            holder.binding.txtStatusTransaction.setTextColor(successColor)
        } else {
            holder.binding.txtStatusTransaction.text = response.status
            holder.binding.txtStatusTransaction.setTextColor(errorColor)
        }
    }

    override fun getItemCount() = transactionResponse.size

}