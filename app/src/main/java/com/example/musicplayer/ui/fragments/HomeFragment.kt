package com.example.musicplayer.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.adapters.SongAdapter
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.other.Status
import com.example.musicplayer.ui.viewmodels.MainViewModel
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
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        subscribeToObservers()
        songAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecyclerView() = binding.rvAllSongs.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(context)
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
    }
}