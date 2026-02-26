package ru.vkdev.greentest.ui.list

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vkdev.greentest.repository_api.Repository

internal class ApplicationsListViewModel(
    private val repository: Repository, app: Application
) : AndroidViewModel(app) {

    private val logTag = this::class.simpleName

    private var allApplications: List<UiAppInfo> = emptyList()

    val uiState: StateFlow<UiState>
        field = MutableStateFlow<UiState>(UiState.Loading)

    private fun updateStateWithData(runnableOnly: Boolean? = null) {
        uiState.update { existing ->
            val state = existing as? UiState.ScreenData ?: UiState.ScreenData(emptyList(), false)

            state.copy(
                applications = filterApplications(runnableOnly ?: state.runnableOnly),
                runnableOnly = runnableOnly ?: state.runnableOnly
            )
        }
    }

    fun startLoading() {
        uiState.value = UiState.Loading
        viewModelScope.launch(IO) {
            repository.installedAppsBaseInfo(application)
                .onFailure { error ->
                    Log.e(logTag, error.stackTraceToString())
                    allApplications = emptyList()

                    uiState.emit(UiState.Error)
                }.onSuccess { result ->
                    allApplications = result.map {
                        UiAppInfo(appName = it.appName ?: it.packageId, packageId = it.packageId, hasLaunchedActivity = it.hasLaunchedActivity)
                    }.sortedBy { it.appName }

                    updateStateWithData()
                }
        }
    }

    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.ShowRunnableOnlyIntent -> {
                if (uiState.value is UiState.ScreenData) {
                    updateStateWithData(intent.isRunnableOnly)
                } else {
                    Log.w(logTag, "Invalid UI state")
                }
            }

            is Intent.OpenDetailsScreenIntent -> {
                //todo
            }
        }
    }

    suspend fun requestAppIcon(packageId: String) = withContext(Dispatchers.IO) {
        repository.imageIcon(context = application, packageId)
    }

    private fun filterApplications(isRunnableOnly: Boolean) =
        if (isRunnableOnly) allApplications.filter { it.hasLaunchedActivity } else allApplications

    @Stable
    internal data class UiAppInfo(
        val appName: String, val packageId: String, val hasLaunchedActivity: Boolean
    )

    internal sealed interface UiState {
        object Loading : UiState
        object Error : UiState

        @Stable
        data class ScreenData(
            val applications: List<UiAppInfo>, val runnableOnly: Boolean
        ) : UiState
    }

    sealed interface Intent {
        data class ShowRunnableOnlyIntent(val isRunnableOnly: Boolean) : Intent
        data class OpenDetailsScreenIntent(val uiAppInfo: UiAppInfo) : Intent
    }
}