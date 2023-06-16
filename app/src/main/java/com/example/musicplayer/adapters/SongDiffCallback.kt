package com.example.musicplayer.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.musicplayer.data.entities.Song

object SongDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.id == newItem.id
    }
}
