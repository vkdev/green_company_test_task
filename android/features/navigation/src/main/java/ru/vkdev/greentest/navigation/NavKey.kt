package ru.vkdev.greentest.navigation

sealed interface NavKey {
    data object ApplicationsList : NavKey
    data class ApplicationDetails(val packageId: String) : NavKey
}