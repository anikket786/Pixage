package com.app.general.pixage.images.data.repository

import android.content.Context
import com.app.general.pixage.BuildConfig
import com.app.general.pixage.R
import com.app.general.pixage.common.util.encodeStringToUrl
import com.app.general.pixage.common.util.serializeToMap
import com.app.general.pixage.images.data.di.IoDispatcher
import com.app.general.pixage.images.data.local.ImagePostsDao
import com.app.general.pixage.images.data.local.model.LocalImagePost
import com.app.general.pixage.images.data.remote.PixabayApiService
import com.app.general.pixage.images.data.remote.model.RemoteImagePostsParam
import com.app.general.pixage.images.domain.entity.ImagePost
import com.app.general.pixage.images.domain.entity.ImagePostsParam
import com.app.general.pixage.images.domain.repository.ImagePostsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@ActivityRetainedScoped
class ImagePostsRepositoryImpl @Inject constructor(
    private val pixabayApiService: PixabayApiService,
    private val imagePostsDao: ImagePostsDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationContext private val appContext: Context,
) : ImagePostsRepository {
    override suspend fun getImagePosts(limit: Int, startIndex: Int) : List<ImagePost>? {
        return withContext(dispatcher) {
            val posts = imagePostsDao.getImagePosts(limit, startIndex)?.map {
                ImagePost(
                    id = it.id,
                    tags = it.tags,
                    user = it.user,
                    likes = it.likes,
                    downloads = it.downloads,
                    comments = it.comments,
                    previewUrl = it.previewUrl,
                    largeImageUrl = it.largeImageUrl,
                )
            }
            return@withContext posts
        }
    }

    override suspend fun loadImagePosts(param: ImagePostsParam) {
        withContext(dispatcher) {
            try {
                refreshImageUpdateCache(
                    RemoteImagePostsParam(
                        key = BuildConfig.PIXABAY_API_KEY,
                        searchText = encodeStringToUrl(param.searchText ?: ""),
                        imageType = param.imageType,
                        page = param.page,
                        perPage = param.perPage,
                    )
                )
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException,
                    is UnknownHostException,
                    is ConnectException -> {
                        // In case of this network error let the data source display cached
                        // image posts instead of throwing an y exception
                    }
                    else -> throw Exception(
                        appContext.getString(R.string.something_went_wrong),
                    )
                }
            }
        }
    }

    private suspend fun refreshImageUpdateCache(param: RemoteImagePostsParam) {
        val remoteImages = pixabayApiService.getImages(param.serializeToMap())

        if (param.page == 1) {
            // Clear all the previous rows from the table to prevent mixing of
            // different image types to maintain consistency
            imagePostsDao.clearImagePosts()
        }
        imagePostsDao.insertImagePosts(remoteImages.hits?.map {
            LocalImagePost(
                tags = it?.tags,
                user = it?.user,
                likes = it?.likes,
                downloads = it?.downloads,
                comments = it?.comments,
                previewUrl = it?.previewUrl,
                largeImageUrl = it?.largeImageUrl,
            )
        })
    }
}