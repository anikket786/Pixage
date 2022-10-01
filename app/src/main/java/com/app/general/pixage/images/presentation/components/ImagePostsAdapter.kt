package com.app.general.pixage.images.presentation.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.general.pixage.R
import com.app.general.pixage.databinding.ItemImagePostBinding
import com.app.general.pixage.images.domain.entity.ImagePost
import com.app.general.pixage.images.presentation.state.ImagePostClickListener
import javax.inject.Inject

class ImagePostsAdapter @Inject constructor(
    private val imagePostClickListener: ImagePostClickListener,
) :
    PagingDataAdapter<ImagePost, ImagePostViewHolder>(Comparator) {

    override fun onBindViewHolder(holder: ImagePostViewHolder, position: Int) {
        getItem(position)?.let { userItemUiState -> holder.bind(userItemUiState) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePostViewHolder {
        val binding = inflate<ItemImagePostBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_image_post,
            parent,
            false
        )

        return ImagePostViewHolder(binding, imagePostClickListener)
    }

    object Comparator : DiffUtil.ItemCallback<ImagePost>() {
        override fun areItemsTheSame(oldItem: ImagePost, newItem: ImagePost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ImagePost,
            newItem: ImagePost,
        ): Boolean {
            return oldItem == newItem
        }
    }
}