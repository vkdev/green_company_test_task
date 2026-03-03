package ru.vkdev.greentest.ui.appdetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import ru.vkdev.greentest.ui.api.appdetails.ApplicationDetailsScreenFactory

private class DefaultScreenFactory : ApplicationDetailsScreenFactory {
    @Composable
    override fun ScreenContent(paddingValues: PaddingValues, packageId: String) = ApplicationDetailsScreen(
        paddingValues = paddingValues,
        packageId = packageId
    )
}

val applicationDetailsScreenFactory: ApplicationDetailsScreenFactory = DefaultScreenFactory()