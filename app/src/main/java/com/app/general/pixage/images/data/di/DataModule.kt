package com.app.general.pixage.images.data.di

import android.content.Context
import androidx.room.Room
import com.app.general.pixage.BuildConfig
import com.app.general.pixage.common.constants.EndPoints
import com.app.general.pixage.images.data.local.ImagePostsDao
import com.app.general.pixage.images.data.local.ImagePostsDb
import com.app.general.pixage.images.data.remote.PixabayApiService
import com.app.general.pixage.images.data.repository.ImagePostsRepositoryImpl
import com.app.general.pixage.images.domain.repository.ImagePostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideRoomDao(database: ImagePostsDb): ImagePostsDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): ImagePostsDb {
        return Room.databaseBuilder(
            appContext,
            ImagePostsDb::class.java,
            ImagePostsDb.DB_NAME,
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(EndPoints.PIXABAY_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): PixabayApiService {
        return retrofit.create(PixabayApiService::class.java)
    }

    @Provides
    fun provideImagePosterRepository(
        pixabayApiService: PixabayApiService,
        imagePostsDao: ImagePostsDao,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        @ApplicationContext appContext: Context,
    ) : ImagePostsRepository {
        return ImagePostsRepositoryImpl(
            pixabayApiService,
            imagePostsDao,
            dispatcher,
            appContext,
        )
    }
}