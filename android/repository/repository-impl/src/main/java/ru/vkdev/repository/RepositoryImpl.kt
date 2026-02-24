package ru.vkdev.repository

import android.content.Context
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.repository.source.AppInfoDataLoader

class RepositoryImpl : Repository {
    override fun installedAppsBaseInfo(context: Context) = AppInfoDataLoader.installedAppsBaseInfo(context)
    override fun imageIcon(context: Context, packageId: String) = AppInfoDataLoader.imageIcon(context, packageId)
}