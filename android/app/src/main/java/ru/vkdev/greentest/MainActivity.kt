package ru.vkdev.greentest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.vkdev.greentest.navigation.AppNavGraph
import ru.vkdev.greentest.navigation.NavKey
import ru.vkdev.greentest.ui.appdetails.applicationDetailsScreenFactory
import ru.vkdev.greentest.ui.list.applicationsListScreenFactory
import ru.vkdev.greentest.ui_common.theme.GreenTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenTestTheme {
                val backStack = remember { mutableStateListOf<NavKey>(NavKey.ApplicationsList) }
                BackHandler(enabled = backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavGraph(
                        innerPadding = innerPadding,
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        appsListScreenFactory = applicationsListScreenFactory,
                        appDetailsScreenFactory = applicationDetailsScreenFactory
                    )
                }
            }
        }
    }
}