package ru.vkdev.greentest.ui.api.appdetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

interface ApplicationDetailsScreenFactory {
    @Composable
    fun ScreenContent(
        paddingValues: PaddingValues,
        packageId: String
    )
}
