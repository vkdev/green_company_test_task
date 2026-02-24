package ru.vkdev.greentest

import android.app.Application
import ru.vkdev.greentest.di.startKoinIfNotStarted

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoinIfNotStarted()
    }
}