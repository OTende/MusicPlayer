package com.example.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.databinding.SwipeItemBinding
import javax.inject.Inject

class SwipeSongAdapter @Inject constructor() : BaseSongAdapter<SwipeItemBinding>() {
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ): SwipeItemBinding {
        return SwipeItemBinding.inflate(inflater, parent, attachToParent)
    }

    override fun bind(holder: SongViewHolder, song: Song) {
        with(holder.binding) {
//            val text = "${song.title} - ${song.author}"
            tvPrimary.text = song.title

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}