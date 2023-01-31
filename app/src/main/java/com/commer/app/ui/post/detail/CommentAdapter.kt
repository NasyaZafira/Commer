package com.commer.app.ui.post.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commer.app.R
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.post.detail.KomentarPost
import com.commer.app.databinding.ItemCommentPeopleBinding
import com.commer.app.ui.TimeAgo.toTimeAgo

class CommentAdapter(
    val allResponse: MutableList<KomentarPost>,
    private val onItemClick: (KomentarPost) -> Unit,
    private var optionsMenuClickListener: OptionsMenuClickListener
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    interface OptionsMenuClickListener {
        fun onOptionsMenuOtherUserClicked(view: View, idComment: Int)
        fun onOptionsMenuUserClicked(view: View, idComment: Int, position: Int)
    }

    inner class ViewHolder(val binding: ItemCommentPeopleBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemCommentPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = allResponse[position]
        val getIdUser = CommerSharedPref.userId
        if (response.idUser.profilePic.isNullOrEmpty()) {
            Glide.with(holder.itemView)
                .load(R.drawable.img_user)
                .into(holder.binding.imgPeople)
        } else {
            Glide.with(holder.itemView)
                .load(response.idUser.profilePic)
                .into(holder.binding.imgPeople)
        }
        holder.binding.txtFullName.text = response.idUser.fullname
        if (response.idUser.status == "Verified" ||
            response.idUser.status == "Official") {
            holder.binding.icVerified.visibility = View.VISIBLE
        }
        if (response.idUser.id != getIdUser) {
            holder.binding.constraintUser.setOnClickListener {
                onItemClick(response)
            }
        }
        holder.binding.txtPeopleInterest.text = response.idUser.passion
        holder.binding.txtPostTime.text = response.createdDate.toTimeAgo()
        holder.binding.txtComment.text = response.isiKomentar
        holder.binding.btnMore.setOnClickListener {
            if (response.idUser.id == getIdUser) {
                optionsMenuClickListener.onOptionsMenuUserClicked(it, response.idKomentar, position)
            } else {
                optionsMenuClickListener.onOptionsMenuOtherUserClicked(it, response.idKomentar)
            }
        }
    }

    override fun getItemCount() = allResponse.size

}