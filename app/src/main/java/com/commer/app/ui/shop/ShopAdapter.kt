package com.commer.app.ui.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commer.app.R
import com.commer.app.data.model.remote.shop.Data
import com.commer.app.databinding.ItemShopBinding

class ShopAdapter(
    var product: MutableList<Data>,
    private val onDetail: (Data) -> Unit
): RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemShopBinding) :
            RecyclerView.ViewHolder(binding.root){
                fun bind(
                    listShop : List<Data>,
                    onDetail: (Data) -> Unit
                ) {
                    val getProduct = listShop[position]

                    Glide.with(binding.imgProduct.context)
                        .load(getProduct.images )
                        .error(R.drawable._995)
                        .into(binding.imgProduct)

                    binding.isName.text = getProduct.name
                    binding.isPrice.text = getProduct.price
                    binding.itemShop.setOnClickListener {
                        onDetail(getProduct)
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(product, onDetail)
    }
    override fun getItemCount() = product.size

}