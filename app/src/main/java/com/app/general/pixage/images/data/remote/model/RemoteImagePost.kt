package com.app.general.pixage.images.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteImagePostsResponse(
    val total: Int?,
    val totalHits: Int?,
    val hits: List<RemoteImagePost?>?,
) {
    data class RemoteImagePost(
        val tags: String?,
        val user: String?,
        val likes: Int?,
        val downloads: Int?,
        val comments: Int?,

        @SerializedName("previewURL")
        val previewUrl: String?,

        @SerializedName("largeImageURL")
        val largeImageUrl: String?,
    )
}
