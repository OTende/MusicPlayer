package com.example.musicplayer.di

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.MainActivity
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)

//    fun injectContext(musicApplication: MusicApplication)
}

@Module(includes = [GlideModule::class])
class AppModule(private val application: MusicApplication) {

    @Provides
    @Singleton
    fun provideApplication(): MusicApplication = application
}

@Module
object GlideModule {
    @Provides
    @Singleton
    fun provideGlideRequestManage(application: MusicApplication): RequestManager {
        return Glide.with(application).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_loading_image)
                .error(R.drawable.ic_loading_image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
        )
    }
}