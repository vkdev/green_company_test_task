package ru.vkdev.repository

import android.content.Context
import android.graphics.Bitmap
import ru.vkdev.greentest.cacheapi.InmemoryLruCache
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.repository.source.AppInfoDataLoader
import ru.vkdev.repository.source.toBitmap

class RepositoryImpl(
    private val bitmapCache: InmemoryLruCache<String, Bitmap>
) : Repository {

    override fun installedAppsBaseInfo(context: Context) = AppInfoDataLoader.installedAppsBaseInfo(context)

    override fun imageIcon(context: Context, packageId: String): Bitmap? {
        return bitmapCache.get(packageId) ?: run {
            return synchronized(this) {
                val cached = bitmapCache.get(packageId)
                if (cached == null) {
                    val newIcon = AppInfoDataLoader.imageIcon(context, packageId)?.toBitmap()?.also {
                        bitmapCache.put(packageId, it)
                    }
                    newIcon
                } else {
                    cached
                }
            }
        }
    }
}