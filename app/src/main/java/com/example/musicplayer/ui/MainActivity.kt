package com.example.musicplayer.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.exoplayer.MusicService
import com.example.musicplayer.ui.viewmodels.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        (applicationContext as MusicApplication).appComponent.inject(this)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicService::class.java))
    }
}