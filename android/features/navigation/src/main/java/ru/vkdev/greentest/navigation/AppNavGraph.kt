package ru.vkdev.greentest.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import ru.vkdev.greentest.iu.api.appdetails.ApplicationDetailsScreenFactory
import ru.vkdev.greentest.iu.api.list.ApplicationsListScreenFactory

@Suppress("MutableParameter")
@Composable
fun AppNavGraph(
    innerPadding: PaddingValues,
    backStack: MutableList<NavKey>,
    onBack: () -> Unit,
    appsListScreenFactory: ApplicationsListScreenFactory,
    appDetailsScreenFactory: ApplicationDetailsScreenFactory,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        modifier = modifier.fillMaxSize(),
        backStack = backStack,
        onBack = onBack,
        entryProvider = { key ->
            when (key) {
                is NavKey.ApplicationsList -> NavEntry(key) {
                    appsListScreenFactory.ScreenContent(
                        paddingValues = innerPadding,
                        onAppClick = { packageId ->
                            backStack.add(NavKey.ApplicationDetails(packageId))
                        }
                    )
                }
                is NavKey.ApplicationDetails -> NavEntry(key) {
                    appDetailsScreenFactory.ScreenContent(
                        paddingValues = innerPadding,
                        packageId = key.packageId
                    )
                }
            }
        }
    )
}
