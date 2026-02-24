package ru.vkdev.greentest.repository_api.model

data class AppInfo(
    val appName: String?,
    val version: String?,
    val packageId: String,
    val versionLong: Long?,
    val hasLaunchedActivity: Boolean,
)