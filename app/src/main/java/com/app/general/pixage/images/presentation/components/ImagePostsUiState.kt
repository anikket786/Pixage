package com.app.general.pixage.images.presentation.components

import android.content.Context
import androidx.paging.LoadState
import com.app.general.pixage.R
import com.app.general.pixage.common.ui.BaseUiState

data class ImagePostsUiState(
    private val loadState: LoadState
) : BaseUiState() {

    fun getProgressBarVisibility() = getViewVisibility(isVisible = loadState is LoadState.Loading)

    fun getListVisibility() = getViewVisibility(isVisible = loadState is LoadState.NotLoading)

    fun getErrorVisibility() = getViewVisibility(isVisible = loadState is LoadState.Error)

    fun getErrorMessage(context: Context) = if (loadState is LoadState.Error) {
        if (loadState.error.message.isNullOrEmpty()) {
            context.getString(R.string.no_results_found)
        } else {
            loadState.error.message ?: context.getString(R.string.something_went_wrong)
        }
    } else ""
}