package com.commer.app.ui.post.newpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.commer.app.R
import com.commer.app.databinding.ItemFilesAtNewPostBinding
import java.io.File

class FilesAdapter(
    var selectedFiles: MutableList<File>
) : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFilesAtNewPostBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemFilesAtNewPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val files = selectedFiles[position]
        Glide.with(holder.itemView)
            .load(files.path)
            .transform(CenterCrop(), RoundedCorners(8))
            .placeholder(R.drawable.img_files)
            .into(holder.binding.imgFiles)
    }

    override fun getItemCount() = selectedFiles.size

}