package ru.vkdev.greentest.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

@Suppress("MutableParameter")
@Composable
fun AppNavGraph(
    innerPadding: PaddingValues,
    backStack: MutableList<NavKey>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
        entryProvider = { key ->
            when (key) {
                is NavKey.ApplicationsList -> NavEntry(key) {
                    //todo
                }
                is NavKey.ApplicationDetails -> NavEntry(key) {
                    //todo
                }
                else -> error("Unknown route: $key")
            }
        }
    )
}
