package com.commer.app.ui.settings.bookmarks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

class BookmarksAdapter(
    var postResponse: MutableList<Data>,
    private val onItemClick: (Data) -> Unit,
    private val onVideoClick: (Data) -> Unit,
    private val onProfileClick: (Data) -> Unit,
    private var onClickedAtBookmarks: OnClickedAtBookmarks
) : RecyclerView.Adapter<BookmarksAdapter.ViewHolder>() {

    interface OnClickedAtBookmarks {
        fun onOptionsMenuOtherUserClicked(
            view: View,
            idPost:
            Int,
            text: String,
            title: String,
            image: String?
        )
        fun onOptionsMenuUserClicked(
            view: View,
            idPost: Int,
            position: Int,
            text: String,
            title: String,
            image: String?,
            dataPost: EditPostResponse
        )
        fun onOptionsMenuSimplerOtherUserClicked(
            view: View,
            idPost: Int
        )
        fun onOptionsMenuSimplerUserClicked(
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
        val response = postResponse[position]
        val getIdUser = CommerSharedPref.userId
        var isVideo = false
        val imageList = ArrayList<SlideModel>().apply {
            response.filePosts.map {
                add(SlideModel(it.url))
                if (it.url.split(".").last() == "mp4") isVideo = true
            }
        }
        val imageSlider = holder.binding.imgSlider
        holder.binding.itemPostAtHome.setOnClickListener {
            onItemClick(response)
        }
        if (response.user.id == getIdUser) {
            holder.binding.constraintUser.setOnClickListener {
                onItemClick(response)
            }
        } else {
            holder.binding.constraintUser.setOnClickListener {
                onProfileClick(response)
            }
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
        holder.binding.txtFullName.text = response.user.fullname
        holder.binding.txtPeopleInterest.text = response.user.passion
        holder.binding.txtPostTime.text = response.createdDate.toTimeAgo()
        if (response.postStatus) {
            holder.binding.icPublicPost.setImageResource(R.drawable.ic_outlined_simpler)
        } else {
            holder.binding.icPublicPost.setImageResource(R.drawable.ic_public_post)
        }
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
        if (imageList.isNotEmpty()) {
            if (imageList.size == 1 && isVideo) {
                holder.binding.imgSlider.visibility = View.GONE
                holder.binding.videoPreview.visibility = View.VISIBLE
                Glide.with(holder.itemView)
                    .load(imageList[0].imageUrl)
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
            onClickedAtBookmarks.onLikeClicked(response.idPost, position)
        }
        holder.binding.icSolidLove.setImageResource(R.drawable.ic_solid_love)
        holder.binding.icSolidLove.setOnClickListener {
            onClickedAtBookmarks.onUnlikeClicked(response.idPost, position)
        }
        holder.binding.txtTotalLove.text = response.totalLike.toString()
        holder.binding.icComment.setImageResource(R.drawable.ic_comment)
        holder.binding.icComment.setOnClickListener {
            onItemClick(response)
        }
        holder.binding.txtTotalComment.text = response.totalKomentar.toString()
        holder.binding.icSavePost.setImageResource(R.drawable.ic_save_post)
        holder.binding.icSavePost.setOnClickListener {
            onClickedAtBookmarks.onBookmarkClicked(response.idPost, position)
        }
        holder.binding.icSolidSavePost.setImageResource(R.drawable.ic_solid_save_post)
        holder.binding.icSolidSavePost.setOnClickListener {
            onClickedAtBookmarks.onDeleteBookmarkClicked(response.idPost, position)
        }
        val dataPost = EditPostResponse(
            response.idPost,
            response.postDesc,
            response.postStatus,
            response.postTags,
            null,
            null
        )
        holder.binding.btnMore.setOnClickListener {
            if (response.user.id == getIdUser) {
                if (response.filePosts.isNotEmpty()) {
                    if (response.postStatus) {
                        onClickedAtBookmarks.onOptionsMenuSimplerUserClicked(it, response.idPost, position, dataPost)
                    } else {
                        onClickedAtBookmarks.onOptionsMenuUserClicked(
                            it,
                            response.idPost,
                            position,
                            response.postDesc,
                            response.user.fullname,
                            response.filePosts.first().url,
                            dataPost
                        )
                    }
                } else {
                    if (response.postStatus) {
                        onClickedAtBookmarks.onOptionsMenuSimplerUserClicked(it, response.idPost, position, dataPost)
                    } else {
                        onClickedAtBookmarks.onOptionsMenuUserClicked(
                            it,
                            response.idPost,
                            position,
                            response.postDesc,
                            response.user.fullname,
                            "",
                            dataPost
                        )
                    }
                }
            } else {
                if (response.filePosts.isNotEmpty()) {
                    if (response.postStatus) {
                        onClickedAtBookmarks.onOptionsMenuSimplerOtherUserClicked(it, response.idPost)
                    } else {
                        onClickedAtBookmarks.onOptionsMenuOtherUserClicked(
                            it,
                            response.idPost,
                            response.postDesc,
                            response.user.fullname,
                            response.filePosts.first().url
                        )
                    }
                } else {
                    if (response.postStatus) {
                        onClickedAtBookmarks.onOptionsMenuSimplerOtherUserClicked(it, response.idPost)
                    } else {
                        onClickedAtBookmarks.onOptionsMenuOtherUserClicked(
                            it,
                            response.idPost,
                            response.postDesc,
                            response.user.fullname,
                            ""
                        )
                    }
                }
            }
        }
    }

    override fun getItemCount() = postResponse.size

}