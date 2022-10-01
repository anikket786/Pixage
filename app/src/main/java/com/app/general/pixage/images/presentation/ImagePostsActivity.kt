package com.app.general.pixage.images.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.general.pixage.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePostsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen() // install app splash screen before calling setContentView
        setContentView(R.layout.activity_image_posts)
    }
}