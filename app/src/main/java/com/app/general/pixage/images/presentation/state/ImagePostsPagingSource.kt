package com.app.general.pixage.images.presentation.state

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.general.pixage.images.domain.entity.ImagePost
import com.app.general.pixage.images.domain.entity.ImagePostsParam
import com.app.general.pixage.images.domain.usecase.GetImagePostsUseCase

class ImagePostsPagingSource(
    private val getImagePostsUseCase: GetImagePostsUseCase,
    private val searchText: String
) : PagingSource<ImagePostsParam, ImagePost>() {

    override suspend fun load(
        params: LoadParams<ImagePostsParam>,
    ): LoadResult<ImagePostsParam, ImagePost> {
        return try {
            val posts = params.key?.let {
                getImagePostsUseCase.invoke(it)
            }

            val prevKey = if (params.key?.page == STARTING_PAGE_INDEX) {
                null
            } else {
                ImagePostsParam(
                    page = params.key?.page?.minus(1),
                    perPage = params.key?.perPage,
                    searchText = params.key?.searchText,
                )
            }

            val nextKey = ImagePostsParam(
                page = params.key?.page?.plus(1),
                perPage = params.key?.perPage,
                searchText = params.key?.searchText,
            )

            return if (posts?.isNotEmpty() == true) {
                LoadResult.Page(
                    data = posts,
                    prevKey = prevKey,
                    nextKey = nextKey,
                )
            } else {
                LoadResult.Error(Exception(""))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<ImagePostsParam, ImagePost>): ImagePostsParam {
        return ImagePostsParam(
            searchText = searchText,
            page = STARTING_PAGE_INDEX,
            perPage = PAGE_SIZE
        )
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
        const val PAGE_SIZE = 20
    }
}