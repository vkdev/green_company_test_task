package ru.vkdev.greentest.repository_api.model

data class AppInfo(
    val appName: String?,
    val version: String?,
    val packageId: String,
    val versionCode: Long?,
    val hasLaunchedActivity: Boolean,
)