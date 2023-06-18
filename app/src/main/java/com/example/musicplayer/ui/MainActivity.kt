package com.example.musicplayer.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.RequestManager
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.adapters.SwipeSongAdapter
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.exoplayer.MusicService
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.exoplayer.toSong
import com.example.musicplayer.other.Status.ERROR
import com.example.musicplayer.other.Status.SUCCESS
import com.example.musicplayer.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var swipeSongAdapter: SwipeSongAdapter

    @Inject
    lateinit var viewModel: MainViewModel

    private var currentSong: Song? = null
    private var playbackState: PlaybackStateCompat? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        (applicationContext as MusicApplication).appComponent.inject(this)
        setContentView(binding.root)
        subscribeToObservers()
        binding.vpSong.adapter = swipeSongAdapter

        binding.vpSong.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (playbackState?.isPlaying == true) {
                    viewModel.playOrToggleSong(swipeSongAdapter.currentList[position])
                } else {
                    currentSong = swipeSongAdapter.currentList[position]
                }
            }
        })

        binding.ivPlayPause.setOnClickListener {
            currentSong?.let {
                viewModel.playOrToggleSong(it, true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicService::class.java))
    }

    private fun switchViewPagerToCurrentSong(song: Song) {
        val newItemIndex = swipeSongAdapter.currentList.indexOf(song)
        if (newItemIndex != -1) {
            binding.vpSong.currentItem = newItemIndex
            currentSong = song
        }
    }

    private fun subscribeToObservers() {
        viewModel.mediaItems.observe(this) {
            it?.let { result ->
                if (result.status == SUCCESS) {
//                    SUCCESS -> {
                    result.data?.let { songs ->
                        swipeSongAdapter.submitList(songs)
                        if (songs.isNotEmpty()) {
                            glide.load((currentSong ?: songs[0]).imageUrl)
                                .into(binding.ivCurSongImage)
                        }
                        switchViewPagerToCurrentSong(currentSong ?: return@observe)
                    }
//                    }
//                    ERROR -> Unit
//                    LOADING -> Unit
                }
            }
        }

        viewModel.currentSong.observe(this) {
            if (it == null) return@observe

            currentSong = it.toSong()
            glide.load(currentSong?.imageUrl).into(binding.ivCurSongImage)
            switchViewPagerToCurrentSong(currentSong ?: return@observe)
        }

        viewModel.playbackState.observe(this) {
            playbackState = it
            binding.ivPlayPause.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause
                else R.drawable.ic_play
            )
        }

        viewModel.isConnected.observe(this) {
            it?.getContentIfNotHandled()?.let { result ->
                if (result.status == ERROR) {
                    Snackbar.make(
                        binding.rootLayout,
                        result.message ?: "Произошла неизвестная ошибка",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        viewModel.networkError.observe(this) {
            it?.getContentIfNotHandled()?.let { result ->
                if (result.status == ERROR) {
                    Snackbar.make(
                        binding.rootLayout,
                        result.message ?: "Произошла неизвестная ошибка",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}