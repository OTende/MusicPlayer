package com.example.musicplayer

import android.app.Application
import com.example.musicplayer.di.AppComponent
import com.example.musicplayer.di.AppModule
import com.example.musicplayer.di.DaggerAppComponent

class MusicApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build() 
    }
}