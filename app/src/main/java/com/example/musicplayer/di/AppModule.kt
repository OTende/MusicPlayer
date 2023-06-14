package com.example.musicplayer.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.ui.MainActivity
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.exoplayer.MusicService
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.musicplayer.ui.fragments.HomeFragment
import com.example.musicplayer.ui.viewmodels.MainViewModel
import com.example.musicplayer.ui.viewmodels.MainViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, ServiceModule::class])//, FragmentModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(homeFragment: HomeFragment)
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
}

@Module
class FragmentModule(private val homeFragment: HomeFragment) {
    @Provides
    fun provideFragment(): HomeFragment = homeFragment

    @Provides
    fun provideViewModel(factory: MainViewModelFactory, fragment: HomeFragment): MainViewModel {
        return ViewModelProvider(fragment, factory).get(MainViewModel::class.java)
    }
}

//@Component(modules = [FragmentModule::class])
//interface ViewModelModule {
//
//}

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