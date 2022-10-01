package com.app.general.pixage.images.presentation.state

import com.app.general.pixage.images.domain.entity.ImagePost

interface ImagePostClickListener {
    fun setSelectedImagePost(imagePost: ImagePost?)
}