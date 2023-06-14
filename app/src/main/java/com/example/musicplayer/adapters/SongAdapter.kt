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


    private val differ = AsyncListDiffer(this, DiffCallback)

    var songs: List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

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
    val songs1 = listOf<Song>(Song(author = "asd"), Song(title = "asd"))

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        holder.bind(song)
//        holder.textView.text = "123"
    }
}


//class SongAdapter @Inject constructor(
//    private val glide: RequestManager
//) : ListAdapter<Song, MyViewHolder>(DiffCallback) {
//
//    companion object DiffCallback : DiffUtil.ItemCallback<Song>() {
//
//        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        return MyViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
//    }
//
//}

//class MyViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    private val nameTextView: TextView = itemView.findViewById(R.id.tvPrimary)
//
//    fun bind(item: Song) {
//        nameTextView.text = item.title
//    }
//
//    companion object {
//        fun from(parent: ViewGroup): MyViewHolder {
//            val layoutInflater = LayoutInflater.from(parent.context)
//            val view = layoutInflater.inflate(R.layout.list_item, parent, false)
//            return MyViewHolder(view)
//        }
//    }
//}

//class SongAdapter @Inject constructor(
//    private val glide: RequestManager
//) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
//
//    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    private val diffCallback = object : DiffUtil.ItemCallback<Song>() {
//        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
//            return oldItem.hashCode() == newItem.hashCode()
//        }
//    }
//
//    private val differ = AsyncListDiffer(this, diffCallback)
//
//    var songs: List<Song>
//        get() = differ.currentList
//        set(value) = differ.submitList(value)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
//        return SongViewHolder(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.list_item,
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
//        val song = songs[position]
//        holder.itemView.apply {
//            this.findViewById<TextView>(R.id.tvPrimary).text = song.title
//            this.findViewById<TextView>(R.id.tvSecondary).text = song.title
//            glide.load(song.imageUrl).into(findViewById(R.id.ivItemImage))
//
//            setOnClickListener {
//                onItemClickListener?.let { click ->
//                    click(song)
//                }
//            }
//        }
//    }
//
//    private var onItemClickListener: ((Song) -> Unit)? = null
//
//    fun setOnItemClickListener(listener: (Song) -> Unit) {
//        onItemClickListener = listener
//    }
//
//    override fun getItemCount(): Int {
//        return songs.size
//    }
//}
