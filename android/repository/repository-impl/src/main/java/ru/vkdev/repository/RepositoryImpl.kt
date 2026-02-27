package ru.vkdev.repository

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import ru.vkdev.greentest.cacheapi.InmemoryLruCache
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.repository_api.model.AppInfo
import ru.vkdev.repository.source.AppInfoDataLoader
import ru.vkdev.repository.source.resizeBitmapIfNeeded
import ru.vkdev.repository.source.toBitmap

class RepositoryImpl(
    private val bitmapCache: InmemoryLruCache<String, Bitmap>
) : Repository {

    private val iconLoadMutexes = Array(32) { Mutex() }

    private fun mutexFor(packageId: String): Mutex = iconLoadMutexes[packageId.hashCode().and(0x1F /* 11111 */) /* даёт значение от 0 до 31, гарантированно попадаем в границы массива из 32 элементов. */]

    override suspend fun installedAppsBaseInfo(context: Context): Result<List<AppInfo>> = withContext(IO) {
        runCatching { AppInfoDataLoader.installedAppsBaseInfo(context) }
    }

    override suspend fun installedAppBaseInfo(context: Context, packageId: String): Result<AppInfo> = withContext(IO) {
        runCatching {
            AppInfoDataLoader.installedAppBaseInfo(context, packageId)
        }
    }

    override suspend fun imageIcon(context: Context, packageId: String, maxSize: Int): Bitmap? =
        withContext(IO) {
            val hash = "${packageId}__size_${maxSize}"

            bitmapCache.get(hash)?.let { return@withContext it }
            mutexFor(hash).withLock {
                bitmapCache.get(hash)?.let { return@withLock it }
                AppInfoDataLoader.imageIcon(context, packageId)
                    ?.toBitmap()
                    ?.resizeBitmapIfNeeded(maxSize)
                    ?.also { bitmapCache.put(hash, it) }
            }
        }
}