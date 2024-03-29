package com.example.musicplayer.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE
import androidx.core.net.toUri
import com.example.musicplayer.data.remote.MusicDatabase
import com.example.musicplayer.exoplayer.State.STATE_CREATED
import com.example.musicplayer.exoplayer.State.STATE_ERROR
import com.example.musicplayer.exoplayer.State.STATE_INITIALIZED
import com.example.musicplayer.exoplayer.State.STATE_INITIALIZING
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicSource @Inject constructor(
    private val musicDatabase: MusicDatabase
) {
    var songs = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData() = withContext(Dispatchers.Main) {
        state = STATE_INITIALIZING
        val allSongs = musicDatabase.getAllSongs()

        songs = allSongs.map { song ->
            with(song) {
                MediaMetadataCompat.Builder()
                    .putString(METADATA_KEY_MEDIA_ID, id)
                    .putString(METADATA_KEY_ARTIST, author)
                    .putString(METADATA_KEY_DISPLAY_SUBTITLE, author)
                    .putString(METADATA_KEY_TITLE, title)
                    .putString(METADATA_KEY_DISPLAY_TITLE, title)
                    .putString(METADATA_KEY_DISPLAY_ICON_URI, imageUrl)
                    .putString(METADATA_KEY_MEDIA_URI, songUrl)
                    .putString(METADATA_KEY_ALBUM_ART_URI, imageUrl)
                    .build()
            }
        }
        state = STATE_INITIALIZED
    }

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        songs.forEach { song ->
//            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(song.getString(METADATA_KEY_MEDIA_URI).toUri())
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI)))

            concatenatingMediaSource.addMediaSource(mediaSource)

        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = songs.map { song ->
        with(song.description) {
            val description = MediaDescriptionCompat.Builder()
                .setMediaUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
                .setTitle(title)
                .setMediaId(mediaId)
                .setSubtitle(subtitle)
                .setIconUri(iconUri)
                .build()

            MediaBrowserCompat.MediaItem(description, FLAG_PLAYABLE)
        }
    }.toMutableList()

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == STATE_CREATED || state == STATE_INITIALIZING) {
            onReadyListeners.add(action)
            false
        } else {
            action(state == STATE_INITIALIZED)
            true
        }
    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}