package com.example.musicplayer.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.adapters.SwipeSongAdapter
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.databinding.FragmentMusicInfoBinding
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.exoplayer.toSong
import com.example.musicplayer.other.Status
import com.example.musicplayer.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MusicInfoFragment : Fragment() {
    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var swipeSongAdapter: SwipeSongAdapter

    private var _binding: FragmentMusicInfoBinding? = null
    private val binding: FragmentMusicInfoBinding get() = _binding!!

    @Inject
    lateinit var viewModel: MainViewModel

    private var currentSong: Song? = null
    private var playbackState: PlaybackStateCompat? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MusicApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicInfoBinding.inflate(layoutInflater)
        binding.vpSong.adapter = swipeSongAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        binding.vpSong.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (playbackState?.isPlaying == true) {
                    viewModel.playOrToggleSong(swipeSongAdapter.currentList[position])
                } else {
                    currentSong = swipeSongAdapter.currentList[position]
                }
            }
        })

        swipeSongAdapter.setOnSongClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_songFragment
            )
        }

        swipeSongAdapter.setOnSongClickListener {
            currentSong?.let {
                viewModel.playOrToggleSong(it, true)
            }
        }

        binding.ivPlayPause.setOnClickListener {
            currentSong?.let {
                viewModel.playOrToggleSong(it, true)
            }
        }

//        swipeSongAdapter.setOnSongClickListener {
//            currentSong?.let {
//                viewModel.playOrToggleSong(it, true)
//            }
//        }

        swipeSongAdapter.setOnSongClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_songFragment
            )
//            binding.bottomMenu.isVisible = false
        }
    }

    private fun subscribeToObservers() {
        viewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                if (result.status == Status.SUCCESS) {
                    result.data?.let { songs ->
                        swipeSongAdapter.submitList(songs)
                        if (songs.isNotEmpty()) {
                            glide.load((currentSong ?: songs[0]).imageUrl)
                                .into(binding.ivCurSongImage)
                        }
                        switchViewPagerToCurrentSong(currentSong ?: return@observe)
                    }
//                    }
                }
            }
        }

        viewModel.currentSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            currentSong = it.toSong()
            glide.load(currentSong?.imageUrl).into(binding.ivCurSongImage)
            switchViewPagerToCurrentSong(currentSong ?: return@observe)
        }

        viewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            binding.ivPlayPause.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause
                else R.drawable.ic_play
            )
        }

        viewModel.isConnected.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { result ->
                if (result.status == Status.ERROR) {
                    Snackbar.make(
                        binding.root,
                        result.message ?: getString(R.string.unknown_error),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        viewModel.networkError.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { result ->
                if (result.status == Status.ERROR) {
                    Snackbar.make(
                        binding.root,
                        result.message ?: getString(R.string.unknown_error),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun switchViewPagerToCurrentSong(song: Song) {
        val newItemIndex = swipeSongAdapter.currentList.indexOf(song)
        if (newItemIndex != -1) {
            binding.vpSong.currentItem = newItemIndex
            currentSong = song
        }
    }
}