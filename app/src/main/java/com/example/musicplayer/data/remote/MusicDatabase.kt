package com.example.musicplayer.data.remote

import android.util.Log
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.other.Constants.MUSIC_DATABASE
import com.example.musicplayer.other.Constants.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.memoryCacheSettings
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MusicDatabase {
    private val settings = firestoreSettings {
        setLocalCacheSettings(memoryCacheSettings {  })
    }
    private val firestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = settings
    }
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            Log.e(MUSIC_DATABASE, e.message!!)
            emptyList()
        }
    }
}