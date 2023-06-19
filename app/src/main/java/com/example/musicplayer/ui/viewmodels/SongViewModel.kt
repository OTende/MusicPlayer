package com.example.musicplayer.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.exoplayer.MusicService
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.musicplayer.exoplayer.playbackPosition
import com.example.musicplayer.other.Constants.UPDATE_PLAYER_POSITION_INTERVAL
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongViewModel @Inject constructor(
    musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    class Factory @Inject constructor(private val connection: MusicServiceConnection) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SongViewModel(connection) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


    private val playbackState = musicServiceConnection.playbackState

    private val _songDuration = MutableLiveData<Long>()
    val songDuration: LiveData<Long> = _songDuration

    private val _playerPosition = MutableLiveData<Long>()
    val playerPosition: LiveData<Long> = _playerPosition

    init {
        updatePlayerPosition()
    }

    private fun updatePlayerPosition() {
        viewModelScope.launch {
            while (true) {
                val position = playbackState.value?.playbackPosition
                if (playerPosition.value != position) {
                    _playerPosition.postValue(position!!)
                    _songDuration.postValue(MusicService.currentSongDuration)
                }
                delay(UPDATE_PLAYER_POSITION_INTERVAL)
            }
        }
    }
}