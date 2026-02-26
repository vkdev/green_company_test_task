package ru.vkdev.repository.source

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import kotlin.math.roundToInt

internal fun Drawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

internal fun Bitmap.resizeBitmapIfNeeded(maxSize: Int): Bitmap {

    if (width <= maxSize && height <= maxSize) {
        return this
    }

    val ratio = if (width >= height) {
        maxSize.toFloat() / width.toFloat()
    } else {
        maxSize.toFloat() / height.toFloat()
    }

    val newWidth = (width * ratio).roundToInt()
    val newHeight = (height * ratio).roundToInt()

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
}