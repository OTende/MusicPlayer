package com.example.musicplayer.di

import com.example.musicplayer.exoplayer.MusicService
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [ServiceModule::class])
interface ServiceComponent

@Module
class ServiceModule {

    @Provides
    fun provideAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    fun provideExoPlayer(
        service: MusicService,
        audioAttributes: AudioAttributes,
    ) = ExoPlayer.Builder(service).build().apply {
        setAudioAttributes(audioAttributes, true)
        setHandleAudioBecomingNoisy(true)
    }

    @Provides
    fun provideDataSourceFactory(service: MusicService) =
        DefaultDataSource.Factory(service) //Util.getUserAgent(application, "Example Name")

}