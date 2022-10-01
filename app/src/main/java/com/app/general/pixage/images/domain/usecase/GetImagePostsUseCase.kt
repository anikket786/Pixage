package com.app.general.pixage.images.domain.usecase

import com.app.general.pixage.common.util.calculateOffset
import com.app.general.pixage.images.domain.entity.ImagePost
import com.app.general.pixage.images.domain.entity.ImagePostsParam
import com.app.general.pixage.images.domain.repository.ImagePostsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetImagePostsUseCase @Inject constructor(
    private val repository: ImagePostsRepository,
) {
    suspend operator fun invoke(param: ImagePostsParam): List<ImagePost>? {
        repository.loadImagePosts(param)
        val offset = calculateOffset(param.page, param.perPage)
        return repository.getImagePosts(limit = param.perPage ?: 20, startIndex = offset)
    }
}