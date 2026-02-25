package ru.vkdev.greentest.repository_api

import android.content.Context
import android.graphics.Bitmap
import ru.vkdev.greentest.repository_api.model.AppInfo

interface Repository {

    fun installedAppsBaseInfo(context: Context): List<AppInfo>

    fun imageIcon(context: Context, packageId: String): Bitmap?
}