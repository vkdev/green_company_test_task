package ru.vkdev.greentest.cache

import android.graphics.Bitmap
import android.util.LruCache
import ru.vkdev.greentest.cacheapi.InmemoryLruCache

private const val defaultMaxCacheSize = 10 * 1024 * 1024

class BitmapLruCache(private val maxCacheSize: Int = defaultMaxCacheSize) : InmemoryLruCache<String, Bitmap> {

    private val cache = object : LruCache<String, Bitmap>(maxCacheSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount
        }
    }

    override fun put(key: String, value: Bitmap) {
        cache.put(key, value)
    }

    override fun get(key: String): Bitmap? {
        return cache.get(key)
    }

    override fun remove(key: String) {
        cache.remove(key)
    }

    override fun clear() {
        cache.evictAll()
    }

    override fun size(): Int {
        return cache.size()
    }
}