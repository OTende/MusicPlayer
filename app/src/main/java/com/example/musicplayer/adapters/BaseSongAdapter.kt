package com.example.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.musicplayer.data.entities.Song

abstract class BaseSongAdapter<VB : ViewBinding> :
    ListAdapter<Song, BaseSongAdapter<VB>.SongViewHolder>(SongDiffCallback) {

    inner class SongViewHolder(internal val binding: VB) : RecyclerView.ViewHolder(binding.root)

    protected var onItemClickListener: ((Song) -> Unit)? = null

    fun setOnSongClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = inflateViewBinding(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    abstract fun bind(holder: SongViewHolder, song: Song)

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        bind(holder, song)
    }

    abstract fun inflateViewBinding(inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean): VB
}