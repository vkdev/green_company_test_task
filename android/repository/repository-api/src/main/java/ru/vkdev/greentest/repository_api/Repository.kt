package ru.vkdev.greentest.repository_api

import android.content.Context
import android.graphics.Bitmap
import ru.vkdev.greentest.repository_api.model.AppInfo

interface Repository {

    fun installedAppsBaseInfo(context: Context): Result<List<AppInfo>>

    fun installedAppBaseInfo(context: Context, packageId: String): Result<AppInfo>

    fun imageIcon(context: Context, packageId: String): Bitmap?
}