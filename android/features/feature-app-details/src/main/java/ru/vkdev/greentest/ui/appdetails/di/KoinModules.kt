package ru.vkdev.greentest.ui.appdetails.di

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.ui.appdetails.ApplicationDetailsViewModel

val featureApplicationDetailsModule = module {
    viewModel { (packageId: String) ->
        ApplicationDetailsViewModel(app = androidApplication(), repository = get<Repository>(), packageId = packageId)
    }
}