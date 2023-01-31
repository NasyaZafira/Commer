package com.commer.app.ui.simplerpost.official

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.commer.app.R
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.post.editpost.EditPostResponse
import com.commer.app.data.model.remote.post.get.Data
import com.commer.app.databinding.ItemPostsAtHomeBinding
import com.commer.app.ui.TimeAgo.toTimeAgo
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.devs.readmoreoption.ReadMoreOption

class OfficialPostAdapter(
    var officialPost: MutableList<Data>,
    private val onItemClick: (Data) -> Unit,
    private val onVideoClick: (Data) -> Unit,
    private val onProfileClick: (Data) -> Unit,
    private var onClickedAtOfficialPost: OnClickedAtOfficialPost
) : RecyclerView.Adapter<OfficialPostAdapter.ViewHolder>() {

    interface OnClickedAtOfficialPost {
        fun onOptionsMenuOtherUserClicked(view: View, idPost: Int)
        fun onOptionsMenuUserClicked(
            view: View,
            idPost: Int,
            position: Int,
            dataPost: EditPostResponse
        )
        fun onBookmarkClicked(idPost: Int, position: Int)
        fun onDeleteBookmarkClicked(idPost: Int, position: Int)
        fun onLikeClicked(idPost: Int, position: Int)
        fun onUnlikeClicked(idPost: Int, position: Int)
    }

    inner class ViewHolder(val binding: ItemPostsAtHomeBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemPostsAtHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = officialPost[position]
        val getIdUser = CommerSharedPref.userId
        var isVideo = false
        val imageList = ArrayList<SlideModel>().apply {
            response.filePosts.map {
                add(SlideModel(it.url))
                if (it.url.split(".").last() == "mp4") isVideo = true
            }
        }
        holder.binding.itemPostAtHome.setOnClickListener {
            onItemClick(response)
        }
        holder.binding.txtFullName.text = response.user.fullname
        if (response.user.id == getIdUser) {
            holder.binding.constraintUser.setOnClickListener {
                onItemClick(response)
            }
        } else {
            holder.binding.constraintUser.setOnClickListener {
                onProfileClick(response)
            }
        }
        if (response.user.status == "Verified" ||
            response.user.status == "Official") {
            holder.binding.icVerified.visibility = View.VISIBLE
        }
        if (response.liked) {
            holder.binding.icSolidLove.visibility = View.VISIBLE
            holder.binding.icLove.visibility = View.INVISIBLE
        } else {
            holder.binding.icLove.visibility = View.VISIBLE
            holder.binding.icSolidLove.visibility = View.INVISIBLE
        }
        if (response.bookmarked) {
            holder.binding.icSolidSavePost.visibility = View.VISIBLE
            holder.binding.icSavePost.visibility = View.INVISIBLE
        } else {
            holder.binding.icSavePost.visibility = View.VISIBLE
            holder.binding.icSolidSavePost.visibility = View.INVISIBLE
        }
        val dataPost = EditPostResponse(
            response.idPost,
            response.postDesc,
            response.postStatus,
            response.postTags,
            null,
            null
        )
        holder.binding.txtPeopleInterest.text = response.user.passion
        holder.binding.txtPostTime.text = response.createdDate.toTimeAgo()
        holder.binding.icPublicPost.setImageResource(R.drawable.ic_outlined_simpler)
        var lastClickTime: Long = 0
        val doubleClickInterval = 400
        if (response.postDesc.length <= 90) {
            holder.binding.txtDescription.setOnClickListener {
                onItemClick(response)
            }
        } else {
            holder.binding.txtDescription.setOnClickListener {
                val currentClickTime = System.currentTimeMillis()
                if (currentClickTime - lastClickTime <= doubleClickInterval) {
                    onItemClick(response)
                } else {
                    lastClickTime = System.currentTimeMillis()
                }
            }
        }
        val txtDescription = ReadMoreOption.Builder(holder.binding.txtDescription.context)
            .moreLabel("See more")
            .lessLabel("See less")
            .moreLabelColor(R.color.text_secondary)
            .lessLabelColor(R.color.text_secondary)
            .build()
        txtDescription.addReadMoreTo(holder.binding.txtDescription, response.postDesc)
        val imageSlider = holder.binding.imgSlider
        if (imageList.isNotEmpty()) {
            if (imageList.size == 1 && isVideo) {
                holder.binding.imgSlider.visibility = View.GONE
                holder.binding.videoPreview.visibility = View.VISIBLE
                val progress = CircularProgressDrawable(holder.binding.videoPreview.context)
                progress.strokeWidth = 5f
                progress.centerRadius = 30f
                progress.start()
                Glide.with(holder.itemView)
                    .load(imageList[0].imageUrl)
                    .placeholder(progress)
                    .into(holder.binding.videoPreview)
                holder.binding.videoPreview.setOnClickListener {
                    onVideoClick(response)
                }
            } else {
                holder.binding.videoPreview.visibility = View.GONE
                imageSlider.visibility = View.VISIBLE
                imageSlider.setOnClickListener {
                    onItemClick(response)
                }
                imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
            }
        } else {
            imageSlider.visibility = View.GONE
            holder.binding.videoPreview.visibility = View.GONE
        }
        holder.binding.icLove.setImageResource(R.drawable.ic_love)
        holder.binding.icLove.setOnClickListener {
            onClickedAtOfficialPost.onLikeClicked(response.idPost, position)
        }
        holder.binding.icSolidLove.setImageResource(R.drawable.ic_solid_love)
        holder.binding.icSolidLove.setOnClickListener {
            onClickedAtOfficialPost.onUnlikeClicked(response.idPost, position)
        }
        holder.binding.txtTotalLove.text = response.totalLike.toString()
        holder.binding.icComment.setImageResource(R.drawable.ic_comment)
        holder.binding.icComment.setOnClickListener {
            onItemClick(response)
        }
        holder.binding.txtTotalComment.text = response.totalKomentar.toString()
        holder.binding.icSavePost.setImageResource(R.drawable.ic_save_post)
        holder.binding.icSavePost.setOnClickListener {
            onClickedAtOfficialPost.onBookmarkClicked(response.idPost, position)
        }
        holder.binding.icSolidSavePost.setImageResource(R.drawable.ic_solid_save_post)
        holder.binding.icSolidSavePost.setOnClickListener {
            onClickedAtOfficialPost.onDeleteBookmarkClicked(response.idPost, position)
        }
        if (response.user.profilePic.isNullOrEmpty()) {
            Glide.with(holder.itemView)
                .load(R.drawable.img_user)
                .into(holder.binding.imgPeople)
        } else {
            Glide.with(holder.itemView)
                .load(response.user.profilePic)
                .into(holder.binding.imgPeople)
        }
        holder.binding.btnMore.setOnClickListener {
            if (response.user.id == getIdUser) {
                onClickedAtOfficialPost.onOptionsMenuUserClicked(
                    it,
                    response.idPost,
                    position,
                    dataPost
                )
            } else {
                onClickedAtOfficialPost.onOptionsMenuOtherUserClicked(
                    it,
                    response.idPost,
                )
            }
        }
    }

    override fun getItemCount() = officialPost.size

}