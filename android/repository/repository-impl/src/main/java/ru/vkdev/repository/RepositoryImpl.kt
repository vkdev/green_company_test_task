package ru.vkdev.repository

import android.content.Context
import android.graphics.Bitmap
import ru.vkdev.greentest.cacheapi.InmemoryLruCache
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.repository_api.model.AppInfo
import ru.vkdev.repository.source.AppInfoDataLoader
import ru.vkdev.repository.source.toBitmap

class RepositoryImpl(
    private val bitmapCache: InmemoryLruCache<String, Bitmap>
) : Repository {

    //эти операции не suspend, так как по сути они все равно являются блокирующими
    //todo использовать Result
    override fun installedAppsBaseInfo(context: Context) = AppInfoDataLoader.installedAppsBaseInfo(context)

    override fun installedAppBaseInfo(context: Context, packageId: String): Result<AppInfo> {
        return runCatching {
            AppInfoDataLoader.installedAppBaseInfo(context, packageId)
        }
    }

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