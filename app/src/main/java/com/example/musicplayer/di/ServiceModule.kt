package com.example.musicplayer.di

import android.content.Context
import com.example.musicplayer.data.remote.MusicDatabase
import com.example.musicplayer.exoplayer.MusicService
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(service: MusicService)
}

@Module
class ServiceModule(private val context: Context) {
    @Provides
    fun provideContext() = context

    @Provides
    fun provideMusicDatabase() = MusicDatabase()

    @Provides
    fun provideAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    fun provideExoPlayer(
        context: Context,
        audioAttributes: AudioAttributes,
    ) = ExoPlayer.Builder(context).build().apply {
        setAudioAttributes(audioAttributes, true)
        setHandleAudioBecomingNoisy(true)
    }

    @Provides
    fun provideDataSourceFactory(context: Context) =
        DefaultDataSource.Factory(context)

}