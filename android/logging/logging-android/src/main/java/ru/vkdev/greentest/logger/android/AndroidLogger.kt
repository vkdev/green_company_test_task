package ru.vkdev.greentest.logger.android

import android.util.Log
import ru.vkdev.greentest.logger.Logger

class AndroidLogger : Logger {
    override fun i(tag: String, messgae: String) {
        Log.i(tag, messgae)
    }

    override fun w(tag: String, messgae: String) {
        Log.w(tag, messgae)
    }

    override fun e(tag: String, messgae: String) {
        Log.e(tag, messgae)
    }

    override fun e(tag: String, messgae: String, t: Throwable) {
        Log.e(tag, messgae, t)
    }

    override fun e(tag: String, t: Throwable) {
        e(tag, "", t)
    }
}