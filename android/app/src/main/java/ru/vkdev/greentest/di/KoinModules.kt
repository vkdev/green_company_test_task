package ru.vkdev.greentest.di

import android.app.Application
import android.graphics.Bitmap
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.vkdev.greentest.cacheapi.InmemoryLruCache
import ru.vkdev.greentest.cache.BitmapLruCache
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.ui.list.di.featureApplicationsListViewModelModule
import ru.vkdev.repository.RepositoryImpl

@Synchronized
fun Application.startKoinIfNotStarted() {

    if (GlobalContext.getOrNull() != null) return

    startKoin {
        androidContext(applicationContext)

        modules(

            cacheModule,
            repositoryModule,

            //feature modules
            featureApplicationsListViewModelModule
        )
    }
}

val cacheModule = module {
    single<InmemoryLruCache<String, Bitmap>> { BitmapLruCache() }
}

val repositoryModule = module {
    single<Repository> { RepositoryImpl(get()) }
}