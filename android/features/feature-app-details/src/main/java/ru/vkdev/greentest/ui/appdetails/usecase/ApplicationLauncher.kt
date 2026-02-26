package ru.vkdev.greentest.ui.appdetails.usecase

import android.app.Application
import android.content.Intent

internal class ApplicationLauncher(private val application: Application) {
    operator fun invoke(packageId: String): Boolean {
        return application.packageManager.getLaunchIntentForPackage(packageId)?.let {
            application.startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            true
        } ?: false
    }
}