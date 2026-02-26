package ru.vkdev.repository.source

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import ru.vkdev.greentest.repository_api.model.AppInfo

internal object AppInfoDataLoader {

    fun installedAppsBaseInfo(context: Context): List<AppInfo> {

        val packageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(0)

        return packages.map { buildAppInfo(packageManager = packageManager, packageInfo = it) }
    }

    @Throws(Exception::class)
    fun installedAppBaseInfo(context: Context, packageId: String): AppInfo {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo("com.example.app", 0)

        return buildAppInfo(packageManager = packageManager, packageInfo = packageInfo)
    }

    private fun buildAppInfo(packageManager: PackageManager, packageInfo: PackageInfo): AppInfo {
        val appName = packageInfo.applicationInfo?.let {
            packageManager.getApplicationLabel(it).toString().ifEmpty { null }
        } ?: packageInfo.packageName

        val versionName = packageInfo.versionName

        val versionLong = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }

        val packageId = packageInfo.packageName

        val hasLaunchedActivity = packageManager.getLaunchIntentForPackage(packageId) != null

        return AppInfo(
            appName = appName,
            version = versionName,
            packageId = packageId,
            versionCode = versionLong,
            hasLaunchedActivity = hasLaunchedActivity
        )
    }

    fun imageIcon(context: Context, packageId: String): Drawable? = runCatching {
        context.packageManager.getApplicationIcon(
            context.packageManager.getApplicationInfo(packageId, 0)
        )
    }.getOrNull()
}