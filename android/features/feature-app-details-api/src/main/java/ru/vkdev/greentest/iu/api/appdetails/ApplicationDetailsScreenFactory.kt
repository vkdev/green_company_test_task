package ru.vkdev.greentest.iu.api.appdetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

interface ApplicationDetailsScreenFactory {
    @Composable
    fun ScreenContent(
        paddingValues: PaddingValues,
        packageId: String
    )
}