package ru.vkdev.greentest.repository_api

import android.content.Context
import android.graphics.Bitmap
import ru.vkdev.greentest.repository_api.model.AppInfo

interface Repository {

    suspend fun installedAppsBaseInfo(context: Context): Result<List<AppInfo>>

    suspend fun installedAppBaseInfo(context: Context, packageId: String): Result<AppInfo>

    suspend fun imageIcon(context: Context, packageId: String, maxSize: Int): Bitmap?
}