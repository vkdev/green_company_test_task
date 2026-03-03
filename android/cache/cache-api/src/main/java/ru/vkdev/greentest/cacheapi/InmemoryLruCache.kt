package ru.vkdev.greentest.cacheapi

interface InmemoryLruCache<K, V> {

    fun put(key: K, value: V)

    fun get(key: K): V?

    fun remove(key: K)

    fun size(): Int

    fun clear()
}