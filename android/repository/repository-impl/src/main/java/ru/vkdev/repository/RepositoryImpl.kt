package ru.vkdev.repository

import android.content.Context
import android.graphics.Bitmap
import ru.vkdev.greentest.cacheapi.InmemoryLruCache
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.repository_api.model.AppInfo
import ru.vkdev.repository.source.AppInfoDataLoader
import ru.vkdev.repository.source.resizeBitmapIfNeeded
import ru.vkdev.repository.source.toBitmap

class RepositoryImpl(
    private val bitmapCache: InmemoryLruCache<String, Bitmap>
) : Repository {

    private val iconLoadLocks = Array(32) { Any() }

    private fun lockFor(packageId: String): Any = iconLoadLocks[packageId.hashCode().and(0x0F)]

    //эти операции не suspend, так как по сути они все равно являются блокирующими
    override fun installedAppsBaseInfo(context: Context): Result<List<AppInfo>> = runCatching { AppInfoDataLoader.installedAppsBaseInfo(context) }

    override fun installedAppBaseInfo(context: Context, packageId: String): Result<AppInfo> = runCatching {
        AppInfoDataLoader.installedAppBaseInfo(context, packageId)
    }

    override fun imageIcon(context: Context, packageId: String, maxSize: Int): Bitmap? {
        val hash = "${packageId}__size_${maxSize}"

        bitmapCache.get(hash)?.let { return it }
        synchronized(lockFor(hash)) {
            bitmapCache.get(hash)?.let { return it }
            val newIcon = AppInfoDataLoader.imageIcon(context, packageId)?.toBitmap()?.resizeBitmapIfNeeded(maxSize)?.also {
                bitmapCache.put(hash, it)
            }
            return newIcon
        }
    }
}