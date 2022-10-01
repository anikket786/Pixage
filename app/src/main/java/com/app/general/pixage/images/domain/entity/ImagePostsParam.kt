package com.app.general.pixage.images.domain.entity

data class ImagePostsParam(
    val searchText: String?,
    val imageType: String = "photo",
    val page: Int?,
    val perPage: Int?,
)