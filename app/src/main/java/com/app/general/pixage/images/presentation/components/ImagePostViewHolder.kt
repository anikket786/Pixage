package com.app.general.pixage.images.presentation.components

import androidx.recyclerview.widget.RecyclerView
import com.app.general.pixage.common.util.ext.executeWithAction
import com.app.general.pixage.databinding.ItemImagePostBinding
import com.app.general.pixage.images.domain.entity.ImagePost
import com.app.general.pixage.images.presentation.state.ImagePostClickListener

class ImagePostViewHolder(
    private val binding: ItemImagePostBinding,
    private val imagePostClickListener: ImagePostClickListener,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(imagePost: ImagePost) {
        binding.executeWithAction {
            this.imagePost = imagePost
            this.listener = imagePostClickListener
        }
    }
}