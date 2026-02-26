package ru.vkdev.greentest.ui.appdetails.di

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.ui.appdetails.ApplicationDetailsViewModel
import ru.vkdev.greentest.ui.appdetails.usecase.ApplicationLauncher

val featureApplicationDetailsModule = module {
    factory { ApplicationLauncher(application = androidApplication()) }

    viewModel { (packageId: String) ->
        ApplicationDetailsViewModel(
            app = androidApplication(),
            repository = get<Repository>(),
            applicationLauncher = get<ApplicationLauncher>(),

            packageId = packageId
        )
    }
}