package com.app.general.pixage.images.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteImagePostsParam(
    val key: String,

    @SerializedName("q")
    val searchText: String?,

    @SerializedName("image_type")
    val imageType: String,

    val page: Int?,

    @SerializedName("per_page")
    val perPage: Int?,
)
