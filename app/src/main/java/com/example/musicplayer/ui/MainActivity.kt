package com.example.musicplayer.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.exoplayer.MusicService
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as MusicApplication).appComponent.inject(this)
        findViewById<TextView>(R.id.asdf).text = glide.toString()

        val intent = Intent(this, MusicService::class.java)
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        stopService(Intent(this, MusicService::class.java))
    }
}