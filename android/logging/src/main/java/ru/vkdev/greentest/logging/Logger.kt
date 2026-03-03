package ru.vkdev.greentest.logging

interface Logger {
    fun i(tag: String, messgae: String)
    fun w(tag: String, messgae: String)
    fun e(tag: String, messgae: String)
    fun e(tag: String, messgae: String, t: Throwable)
    fun e(tag: String, t: Throwable)
}