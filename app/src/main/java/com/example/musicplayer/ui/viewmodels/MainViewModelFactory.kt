package com.example.musicplayer.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.musicplayer.exoplayer.MusicServiceConnection
import javax.inject.Inject

//class MainViewModelFactory @Inject constructor(private val musicServiceConnection: MusicServiceConnection) :
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
//        return MainViewModel(musicServiceConnection) as T
//    }
//}