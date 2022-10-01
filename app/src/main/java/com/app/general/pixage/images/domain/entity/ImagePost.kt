package com.app.general.pixage.images.domain.entity

data class ImagePost(
    val id: Int?,
    val tags: String?,
    val user: String?,
    val likes: Int?,
    val downloads: Int?,
    val comments: Int?,
    val previewUrl: String?,
    val largeImageUrl: String?,
)