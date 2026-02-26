package ru.vkdev.greentest.ui.appdetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.ui.appdetails.model.UiAppDetails
import ru.vkdev.greentest.ui.appdetails.usecase.ApplicationLauncher

internal class ApplicationDetailsViewModel(
    app: Application,
    private val repository: Repository,
    private val applicationLauncher: ApplicationLauncher,
    val packageId: String
) : AndroidViewModel(app) {

    private val logTag = this::class.simpleName

    val applicationDetails: StateFlow<UiState>
        field = MutableStateFlow<UiState>(UiState.Loading)

    fun startLoading() {
        viewModelScope.launch(IO) {

            applicationDetails.value = UiState.Loading

            val appsResult = repository.installedAppBaseInfo(application, packageId).onFailure {
                Log.e(logTag, it.stackTraceToString())
            }

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

    suspend fun requestAppIcon(packageId: String, maxSize: Int) = withContext(IO) {
        repository.imageIcon(context = application, packageId = packageId, maxSize = maxSize)
    }

    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LaunchAppIntent -> {
                applicationLauncher(intent.packageId)
            }
        }
    }

    internal sealed interface UiState {
        object Loading : UiState
        object Error : UiState
        data class Success(val details: UiAppDetails) : UiState
    }

    sealed interface Intent {
        data class LaunchAppIntent(val packageId: String) : Intent
    }
}