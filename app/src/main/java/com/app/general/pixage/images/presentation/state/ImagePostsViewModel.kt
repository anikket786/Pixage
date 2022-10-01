package com.app.general.pixage.images.presentation.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.app.general.pixage.images.domain.entity.ImagePost
import com.app.general.pixage.images.domain.entity.ImagePostsParam
import com.app.general.pixage.images.domain.usecase.GetImagePostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImagePostsViewModel @Inject constructor(
    private val imagePostsUseCase: GetImagePostsUseCase,
) : ViewModel(), ImagePostClickListener {

    /**
     * Contains query text which needs to be searched on PagingSource refresh action
     */
    var searchText = DEFAULT_SEARCH_TEXT

    val imagePosts = Pager(
        config = PagingConfig(
            pageSize = ImagePostsPagingSource.PAGE_SIZE,
            enablePlaceholders = true,
        ),
        initialKey = ImagePostsParam(
            searchText = DEFAULT_SEARCH_TEXT,
            page = ImagePostsPagingSource.STARTING_PAGE_INDEX,
            perPage = ImagePostsPagingSource.PAGE_SIZE,
        ),
        pagingSourceFactory = {
            ImagePostsPagingSource(imagePostsUseCase, searchText)
        },
    ).flow.cachedIn(viewModelScope)

    /**
     * This live data state is observed in fragments to display the image post detail
     * whenever an image post item is clicked
     */
    private val _selectedPost = MutableLiveData<ImagePost?>()
    val selectedPost: LiveData<ImagePost?>
        get() = _selectedPost

    override fun setSelectedImagePost(imagePost: ImagePost?) {
        _selectedPost.value = imagePost
    }

    companion object {
        private const val DEFAULT_SEARCH_TEXT = "fruits"
    }
}