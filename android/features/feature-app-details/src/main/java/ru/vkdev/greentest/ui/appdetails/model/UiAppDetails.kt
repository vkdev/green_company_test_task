package ru.vkdev.greentest.ui.appdetails.model

data class UiAppDetails(
    val appName: String,
    val packageId: String,
    val version: String,
    val versionCode: Long,
    val hasLaunchedActivity: Boolean,
    val hashSum: String? = null
)