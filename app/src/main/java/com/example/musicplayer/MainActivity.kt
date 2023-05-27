package com.example.musicplayer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as MusicApplication).appComponent.inject(this)
        findViewById<TextView>(R.id.asdf).text = glide.toString()
    }
}