package com.app.general.pixage.images.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.general.pixage.images.data.local.model.LocalImagePost

@Database(
    entities = [LocalImagePost::class],
    version = 3,
    exportSchema = false
)
abstract class ImagePostsDb : RoomDatabase() {
    abstract val dao: ImagePostsDao

    companion object {
        const val DB_NAME = "image_posts_database"
    }
}