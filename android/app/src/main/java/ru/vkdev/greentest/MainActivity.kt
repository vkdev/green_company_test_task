package ru.vkdev.greentest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.vkdev.greentest.navigation.AppNavGraph
import ru.vkdev.greentest.navigation.NavKey
import ru.vkdev.greentest.ui.theme.GreenTestTheme

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
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GreenTestTheme {
        Greeting("Android")
    }
}