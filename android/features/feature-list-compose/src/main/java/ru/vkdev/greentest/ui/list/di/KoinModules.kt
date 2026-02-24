package ru.vkdev.greentest.ui.list.di

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.ui.list.ApplicationsListViewModel

val featureApplicationsListViewModelModule = module {
    viewModel { ApplicationsListViewModel(repository = get<Repository>(), app = androidApplication()) }
}