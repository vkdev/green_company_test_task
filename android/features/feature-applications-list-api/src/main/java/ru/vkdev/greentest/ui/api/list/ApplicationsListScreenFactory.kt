package ru.vkdev.greentest.ui.api.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

interface ApplicationsListScreenFactory {
    @Composable
    fun ScreenContent(
        paddingValues: PaddingValues,
        onAppClick: (String) -> Unit
    )
}