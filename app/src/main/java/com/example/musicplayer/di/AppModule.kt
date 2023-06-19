package com.example.musicplayer.di

//import com.example.musicplayer.ui.viewmodels.MainViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.adapters.SwipeSongAdapter
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.musicplayer.ui.MainActivity
import com.example.musicplayer.ui.fragments.HomeFragment
import com.example.musicplayer.ui.fragments.SongFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, ServiceModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(songFragment: SongFragment)
}

@Module(includes = [GlideModule::class])
class AppModule(private val application: MusicApplication) {

    @Provides
    @Singleton
    fun provideApplication(): MusicApplication = application

    @Provides
    @Singleton
    fun provideMusicServiceConnection(
        application: MusicApplication
    ) = MusicServiceConnection(application)

    @Singleton
    @Provides
    fun provideSwipeSongAdapter() = SwipeSongAdapter()
}

@Module
object GlideModule {
    @Provides
    @Singleton
    fun provideGlideRequestManager(application: MusicApplication): RequestManager {
        return Glide.with(application).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_loading_image)
                .error(R.drawable.ic_loading_image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
        )
    }
}