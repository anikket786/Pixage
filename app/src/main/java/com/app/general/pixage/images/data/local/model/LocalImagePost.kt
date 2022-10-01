package com.app.general.pixage.images.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_posts")
data class LocalImagePost(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val tags: String?,
    val user: String?,
    val likes: Int?,
    val downloads: Int?,
    val comments: Int?,

    @ColumnInfo(name = "preview_url")
    val previewUrl: String?,

    @ColumnInfo(name = "large_image_url")
    val largeImageUrl: String?,
)
