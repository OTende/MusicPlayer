package com.example.musicplayer.ui.fragments

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.view.allViews
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.adapters.SongAdapter
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.databinding.FragmentMusicInfoBinding
import com.example.musicplayer.exoplayer.toSong
import com.example.musicplayer.other.Status
import com.example.musicplayer.ui.viewmodels.MainViewModel
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class HomeFragment : Fragment() {
    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var songAdapter: SongAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MusicApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songAdapter.setOnSongClickListener {
            mainViewModel.playOrToggleSong(it)
        }
        setupRecyclerView()
        subscribeToObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.currentSongIndex =
            songAdapter.currentList.indexOf(mainViewModel.currentSong.value?.toSong())
        _binding = null
    }

    private fun setupRecyclerView() = binding.rvAllSongs.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(context)
        updateColors(mainViewModel.currentSongIndex)
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.allSongsProgressBar.isVisible = false
                    result.data.let { songs ->
                        songAdapter.submitList(songs)
                    }
                }

                Status.ERROR -> Unit
                Status.LOADING -> {
                    binding.allSongsProgressBar.isVisible = true
                }
            }
        }

        mainViewModel.currentSong.observe(viewLifecycleOwner) { song ->
            val currentSongIndex = songAdapter.currentList.indexOf(song?.toSong())
            mainViewModel.currentSongIndex = currentSongIndex

            if (binding.rvAllSongs.size > 0) {
                updateColors(currentSongIndex)
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
////        updateColors(mainViewModel.currentSongIndex)
//    }

    private fun updateColors(index: Int) {
        if (binding.rvAllSongs.size > 0 && songAdapter.currentList.size > 0)
            for (i in 0 until songAdapter.currentList.size) {
                val color = if (i == index) {
                    resources.getColor(
                        R.color.colorAccent,
                        requireActivity().theme
                    )
                } else resources.getColor(
                    R.color.darkBackground,
                    requireActivity().theme
                )
                (binding.rvAllSongs[i] as MaterialCardView).strokeColor = color
            }
    }
}