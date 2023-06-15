package com.commer.app.ui.settings.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.R
import com.commer.app.data.model.remote.shop.history.Data
import com.commer.app.databinding.ItemTransactionHistoryBinding
import com.commer.app.ui.CustomStringFormat.toGlobalMoney
import com.commer.app.ui.TimeAgo.toCustomDate

class TransactionShopAdapter(
    private val transactionShop: List<Data>
) : RecyclerView.Adapter<TransactionShopAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTransactionHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemTransactionHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = transactionShop[position]
        val textTitle = response.productName
        val errorColor = ContextCompat.getColor(holder.itemView.context, R.color.error_color)
        val successColor = ContextCompat.getColor(holder.itemView.context, R.color.success_color)
        holder.binding.txtSimpler.text = textTitle
        holder.binding.txtDate.text = response.date.toCustomDate()
        if (response.isPaid == true) {
            holder.binding.txtStatusTransaction.text = "Telah Dibayar  " + response.amount
            holder.binding.txtStatusTransaction.setTextColor(successColor)
        } else if (response.isPaid == false){
            holder.binding.txtStatusTransaction.text = "Gagal / Belum Bayar"
            holder.binding.txtStatusTransaction.setTextColor(errorColor)
        }
    }

    override fun getItemCount() = transactionShop.size

}