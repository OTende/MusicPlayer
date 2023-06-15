package com.example.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.databinding.ListItemBinding
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private val glide: RequestManager
) : ListAdapter<Song, SongAdapter.SongViewHolder>(DiffCallback) {
    inner class SongViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            with(binding) {
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

    companion object DiffCallback : DiffUtil.ItemCallback<Song>() {
            override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.id == newItem.id
            }
        }


//    private val differ = AsyncListDiffer(this, DiffCallback)

//    var songs: List<Song>
//        get() = differ.currentList
//        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }


    private var onItemClickListener: ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        holder.bind(song)
    }
}