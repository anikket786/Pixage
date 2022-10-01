package com.app.general.pixage.images.domain.repository

import com.app.general.pixage.images.domain.entity.ImagePost
import com.app.general.pixage.images.domain.entity.ImagePostsParam

interface ImagePostsRepository {

    suspend fun getImagePosts(limit: Int, startIndex: Int) : List<ImagePost>?

    suspend fun loadImagePosts(param: ImagePostsParam)
}