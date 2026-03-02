package ru.vkdev.greentest.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import ru.vkdev.greentest.iu.api.list.ApplicationsListScreenFactory

private class DefaultScreenFactory : ApplicationsListScreenFactory {
    @Composable
    override fun ScreenContent(paddingValues: PaddingValues, onAppClick: (String) -> Unit) = ApplicationsListScreen(
        paddingValues = paddingValues,
        onAppClick = onAppClick
    )
}

val applicationsListScreenFactory: ApplicationsListScreenFactory = DefaultScreenFactory()