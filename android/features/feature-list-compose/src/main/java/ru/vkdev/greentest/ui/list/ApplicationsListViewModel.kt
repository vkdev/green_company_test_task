package ru.vkdev.greentest.ui.list

import android.app.Application
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

    private val allApplications = MutableStateFlow<List<UiAppInfo>>(emptyList())

    val iuState: StateFlow<UiState>
        field = MutableStateFlow<UiState>(
            UiState(
                applications = emptyList(), runnableOnly = false
            )
        )

    init {
        viewModelScope.launch {
            allApplications.collect {
                iuState.update {
                    it.copy(
                        applications = filterApplications(it.runnableOnly)
                    )
                }
            }
        }
    }

    fun startLoading() {
        viewModelScope.launch(IO) {
            val applications = repository.installedAppsBaseInfo(application).map {
                UiAppInfo(
                    appName = it.appName ?: it.packageId, packageId = it.packageId, hasLaunchedActivity = it.hasLaunchedActivity
                )
            }.sortedBy { it.appName }

            allApplications.emit(applications)
        }
    }

    fun handleIntent(intent: Intent) {
        when (intent) {
            is ShowRunnableOnlyIntent -> {
                iuState.update {
                    it.copy(
                        runnableOnly = intent.isRunnableOnly, applications = filterApplications(intent.isRunnableOnly)
                    )
                }
            }

            is OpenDetailsScreenIntent -> {
                //todo
            }
        }
    }

    suspend fun requestAppIcon(packageId: String) = withContext(Dispatchers.IO) {
        repository.imageIcon(context = application, packageId)
    }

    private fun filterApplications(isRunnableOnly: Boolean) =
        if (isRunnableOnly) allApplications.value.filter { it.hasLaunchedActivity } else allApplications.value

    @Stable
    internal data class UiAppInfo(
        val appName: String, val packageId: String, val hasLaunchedActivity: Boolean
    )

    @Stable
    internal data class UiState(
        val applications: List<UiAppInfo>, val runnableOnly: Boolean
    )

    sealed interface Intent
    data class ShowRunnableOnlyIntent(val isRunnableOnly: Boolean) : Intent
    data class OpenDetailsScreenIntent(val uiAppInfo: UiAppInfo) : Intent
}