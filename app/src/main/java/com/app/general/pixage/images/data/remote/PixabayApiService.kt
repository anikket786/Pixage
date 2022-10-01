package com.app.general.pixage.images.data.remote

import com.app.general.pixage.images.data.remote.model.RemoteImagePostsResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PixabayApiService {
    @GET(".")
    @JvmSuppressWildcards
    suspend fun getImages(@QueryMap(encoded = true) params: Map<String, Any>): RemoteImagePostsResponse
}