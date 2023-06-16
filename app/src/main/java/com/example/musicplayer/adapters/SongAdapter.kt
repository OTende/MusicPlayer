package com.example.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.databinding.ListItemBinding
import javax.inject.Inject

class SongAdapter @Inject constructor(
    val glide: RequestManager
) : BaseSongAdapter<ListItemBinding>() {
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ): ListItemBinding {
        return ListItemBinding.inflate(inflater, parent, attachToParent)
    }

    override fun bind(holder: SongViewHolder, song: Song) {
        with(holder.binding) {
            tvPrimary.text = song.title
            tvSecondary.text = song.author
            glide.load(song.imageUrl).into(ivItemImage)
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}