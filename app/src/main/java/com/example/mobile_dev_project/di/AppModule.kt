package com.example.mobile_dev_project.di

import android.content.Context
import com.example.mobile_dev_project.data.BooksPaths
import com.example.mobile_dev_project.data.repository.OkHttpDownloader
import com.example.mobile_dev_project.data.repository.ParsingRepository
import com.example.mobile_dev_project.data.repository.TtsRepository
import com.example.mobile_dev_project.data.util.UnzipUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
* tell Hilt this file defines how to build dependencies
* module is where you declare @Provides methods
* I refer these websites:
* 1) Dependency injection with Hilt : https://developer.android.com/training/dependency-injection/hilt-android?utm_source=chatgpt.com
* 2) For singleton : https://stackoverflow.com/questions/67407521/android-hilt-singletoncomponent-vs-the-gof-singleton-instance-design-pattern?utm_source=chatgpt.com
* */

// install this module into the application wide compo
// meaning everything here is available as singletons for the whole app
//@InstallIn -Specifies the lifecycle and scope where the module’s bindings live
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // provide a single app wide BooksPaths built with the App context
    // @Singleton - one instance for the whole app lifetime
    //@ApplicationContext - Hilt qualifier that injects the Application context
    @Provides @Singleton
    fun provideBooksPaths(@ApplicationContext ctx: Context) = BooksPaths(ctx)
    // Provide a singleton OkHttpDownloader
    @Provides @Singleton
    fun provideOkHttpDownloader() = OkHttpDownloader()
    // provide the UnzipUtils object alr obj, but exposing via DI makes testing easier
    @Provides @Singleton
    fun provideUnzipUtils() = UnzipUtils
    //provide ParsingRepository that depends on BooksPaths - Hilt auto injects the provided BooksPaths
    @Provides @Singleton
    fun provideParsingRepository(paths: BooksPaths) = ParsingRepository(paths)

    //This just adds hilt for the Tts
    @Provides @Singleton
    fun provideTtsRepository(@ApplicationContext context: Context): TtsRepository { return TtsRepository(context) }
}
