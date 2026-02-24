package ru.vkdev.greentest.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.ui.list.di.featureApplicationsListViewModelModule
import ru.vkdev.repository.RepositoryImpl

@Synchronized
fun Application.startKoinIfNotStarted() {

    if (GlobalContext.getOrNull() != null) return

    startKoin {
        androidContext(applicationContext)

        modules(

            repositoryModule,

            //feature modules
            featureApplicationsListViewModelModule
        )
    }
}

val repositoryModule = module {
    single<Repository> { RepositoryImpl() }
}