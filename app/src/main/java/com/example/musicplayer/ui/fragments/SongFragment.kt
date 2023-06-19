package com.example.musicplayer.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.RequestManager
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.databinding.FragmentSongBinding
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.exoplayer.toSong
import com.example.musicplayer.other.Status
import com.example.musicplayer.ui.viewmodels.MainViewModel
import com.example.musicplayer.ui.viewmodels.SongViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class SongFragment : Fragment() {
    @Inject
    internal lateinit var glide: RequestManager

    @Inject
    internal lateinit var mainViewModel: MainViewModel

    @Inject
    internal lateinit var songViewModel: SongViewModel

    private var currentSong: Song? = null

    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    private var playbackState: PlaybackStateCompat? = null

    private var shouldUpdateSeekbar = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MusicApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        binding.ivPlayPauseDetail.setOnClickListener {
            currentSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }

        binding.ivSkipPrevious.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }

        binding.ivSkip.setOnClickListener {
            mainViewModel.skipToNextSong()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        currentSong?.let {
            updateTitleAndSongImage(it)
        }
    }

    override fun onStart() {
        super.onStart()
        currentSong?.let {
            updateTitleAndSongImage(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateTitleAndSongImage(song: Song) {
        val title = "${song.title} - ${song.author}"
        binding.tvSongName.text = title
        glide.load(song.imageUrl).into(binding.ivSongImage)
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                if (result.status == Status.SUCCESS) {
                    result.data?.let { songs ->
                        if (currentSong == null && songs.isNotEmpty()) {
                            currentSong = songs[0]
                            updateTitleAndSongImage(songs[0])
                        }
                    }
                }
            }
        }

        mainViewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            binding.ivPlayPauseDetail.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause
                else R.drawable.ic_play
            )
            binding.seekBar.progress = it?.position?.toInt() ?: 0
        }

        mainViewModel.currentSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            currentSong = it.toSong()
            updateTitleAndSongImage(currentSong!!)
        }

        songViewModel.playerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekbar) {
                binding.seekBar.progress = it.toInt()
                setPlayerTimeToTextView(it)
            }
        }

        songViewModel.songDuration.observe(viewLifecycleOwner) {
            binding.seekBar.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            binding.tvSongDuration.text = dateFormat.format(it)
        }
    }

    private fun setPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.tvCurTime.text = dateFormat.format(ms)
    }
}