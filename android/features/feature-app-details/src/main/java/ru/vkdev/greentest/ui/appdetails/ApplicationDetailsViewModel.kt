package ru.vkdev.greentest.ui.appdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.ui.appdetails.model.UiAppDetails

internal class ApplicationDetailsViewModel(
    app: Application,
    repository: Repository,
    val packageId: String
) : AndroidViewModel(app) {

    val applicationDetails: StateFlow<UiState>
        field = MutableStateFlow<UiState>(UiState.Loading)

    init {
        viewModelScope.launch(IO) {

            val appsResult = repository.installedAppBaseInfo(application, packageId)
            applicationDetails.value = UiState.Loading

            appsResult.onFailure {
                applicationDetails.value = UiState.Error
            }.onSuccess {
                applicationDetails.value = UiState.Success(
                    UiAppDetails(
                        appName = it.appName.orEmpty(),
                        packageId = it.packageId,
                        version = it.version.orEmpty(),
                        versionCode = it.versionCode ?: 0L,
                        hasLaunchedActivity = it.hasLaunchedActivity
                    )
                )
            }
        }
    }

    internal sealed interface UiState {
        object Loading : UiState
        object Error : UiState
        data class Success(val details: UiAppDetails) : UiState
    }
}