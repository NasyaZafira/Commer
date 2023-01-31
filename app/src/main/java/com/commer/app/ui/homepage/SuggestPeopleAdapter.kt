package com.commer.app.ui.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commer.app.R
import com.commer.app.data.model.remote.user.suggested.Data
import com.commer.app.databinding.ItemSuggestedPeopleBinding

class SuggestPeopleAdapter(
    private val allUsers: List<Data>,
    private val onItemClick: (Data) -> Unit,
    private var buttonOnClickListener: ButtonOnClickListener
) : RecyclerView.Adapter<SuggestPeopleAdapter.ViewHolder>() {

    interface ButtonOnClickListener {
        fun setButtonFollow(userId: Int)
        fun setButtonUnFollow(userId: Int)
    }

    inner class ViewHolder(val binding: ItemSuggestedPeopleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemSuggestedPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = allUsers[position]
        holder.binding.constraintUser.setOnClickListener {
            onItemClick(user)
        }
        holder.binding.txtFullName.text = user.fullname
        if (user.status == "User" || user.status == "Subscribed") {
            holder.binding.icVerified.visibility = View.GONE
        }
        if (user.isFollow) {
            holder.binding.btnUnFollow.visibility = View.VISIBLE
            holder.binding.btnFollow.visibility = View.INVISIBLE
        } else {
            holder.binding.btnUnFollow.visibility = View.INVISIBLE
            holder.binding.btnFollow.visibility = View.VISIBLE
        }
        holder.binding.btnFollow.setOnClickListener {
            buttonOnClickListener.setButtonFollow(user.id)
            holder.binding.btnUnFollow.visibility = View.VISIBLE
            holder.binding.btnFollow.visibility = View.INVISIBLE
        }
        holder.binding.btnUnFollow.setOnClickListener {
            buttonOnClickListener.setButtonUnFollow(user.id)
            holder.binding.btnUnFollow.visibility = View.INVISIBLE
            holder.binding.btnFollow.visibility = View.VISIBLE
        }
        holder.binding.txtPeopleInterest.text = user.passion
        if (user.profilePic.isNullOrEmpty()) {
            Glide.with(holder.itemView)
                .load(R.drawable.img_user)
                .into(holder.binding.imgPeople)
        } else {
            Glide.with(holder.itemView)
                .load(user.profilePic)
                .into(holder.binding.imgPeople)
        }
    }

    override fun getItemCount() = allUsers.size

}