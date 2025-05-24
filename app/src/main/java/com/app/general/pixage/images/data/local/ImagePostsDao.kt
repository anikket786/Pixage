package com.app.general.pixage.images.data.local

import androidx.room.*
import com.app.general.pixage.images.data.local.model.LocalImagePost

@Dao
interface ImagePostsDao {
    @Query("SELECT * FROM image_posts LIMIT :limit OFFSET :startIndex")
    suspend fun getImagePosts(limit: Int, startIndex: Int): List<LocalImagePost>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImagePosts(restaurants: List<LocalImagePost>)

    @Query("DELETE FROM image_posts")
    suspend fun clearImagePosts()
}