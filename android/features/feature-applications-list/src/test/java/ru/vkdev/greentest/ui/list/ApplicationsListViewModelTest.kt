package ru.vkdev.greentest.ui.list

import android.app.Application
import android.graphics.Bitmap
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.vkdev.greentest.logger.Logger
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.repository_api.model.AppInfo

class ApplicationsListViewModelTest {

    private lateinit var repository: Repository
    private lateinit var application: Application
    private lateinit var logger: Logger

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        application = mockk(relaxed = true)
        logger = mockk(relaxed = true)
    }

    @Test
    fun startLoading_repositoryReturnsList_emitsScreenData() = runBlocking {
        val appList = listOf(
            AppInfo("App A", "1.0", "com.a", 1L, true),
            AppInfo("App B", "2.0", "com.b", 2L, false)
        )
        coEvery { repository.installedAppsBaseInfo(any()) } returns Result.success(appList)

        val viewModel = ApplicationsListViewModel(repository = repository, logger = logger, app = application)

        viewModel.startLoading()
        delay(150)

        val state = viewModel.uiState.value
        assertTrue(state is ApplicationsListViewModel.UiState.ScreenData)
        val data = state as ApplicationsListViewModel.UiState.ScreenData
        assertEquals(2, data.applications.size)
        assertEquals("App A", data.applications[0].appName)
        assertEquals("com.a", data.applications[0].packageId)
        assertEquals(true, data.applications[0].hasLaunchedActivity)
        assertEquals("App B", data.applications[1].appName)
        assertEquals(false, data.applications[1].hasLaunchedActivity)
        assertEquals(false, data.runnableOnly)
    }

    @Test
    fun startLoading_repositoryFails_emitsError() = runBlocking {
        coEvery { repository.installedAppsBaseInfo(any()) } returns
            Result.failure(RuntimeException("Load failed"))

        val viewModel = ApplicationsListViewModel(repository = repository, logger = logger, app = application)

        viewModel.startLoading()
        delay(150)

        assertEquals(ApplicationsListViewModel.UiState.Error, viewModel.uiState.value)
    }

    @Test
    fun startLoading_appsSortedByAppName() = runBlocking {
        val appList = listOf(
            AppInfo("Zebra", null, "com.z", null, false),
            AppInfo("Alpha", null, "com.a", null, true)
        )
        coEvery { repository.installedAppsBaseInfo(any()) } returns Result.success(appList)

        val viewModel = ApplicationsListViewModel(repository = repository, logger = logger, app = application)

        viewModel.startLoading()
        delay(150)

        val data = (viewModel.uiState.value as ApplicationsListViewModel.UiState.ScreenData)
        assertEquals("Alpha", data.applications[0].appName)
        assertEquals("Zebra", data.applications[1].appName)
    }

    @Test
    fun handleIntent_ShowRunnableOnlyIntent_filtersToRunnableOnly() = runBlocking {
        val appList = listOf(
            AppInfo("Runnable", "1.0", "com.r", 1L, true),
            AppInfo("Not Runnable", "1.0", "com.n", 1L, false)
        )
        coEvery { repository.installedAppsBaseInfo(any()) } returns Result.success(appList)

        val viewModel = ApplicationsListViewModel(repository = repository, logger = logger, app = application)
        viewModel.startLoading()
        delay(150)

        viewModel.handleIntent(ApplicationsListViewModel.Intent.ShowRunnableOnlyIntent(isRunnableOnly = true))

        val data = (viewModel.uiState.value as ApplicationsListViewModel.UiState.ScreenData)
        assertEquals(1, data.applications.size)
        assertEquals("Runnable", data.applications[0].appName)
        assertEquals(true, data.runnableOnly)
    }

    @Test
    fun handleIntent_ShowRunnableOnlyIntentFalse_showsAllApps() = runBlocking {
        val appList = listOf(
            AppInfo("Runnable", "1.0", "com.r", 1L, true),
            AppInfo("Not Runnable", "1.0", "com.n", 1L, false)
        )
        coEvery { repository.installedAppsBaseInfo(any()) } returns Result.success(appList)

        val viewModel = ApplicationsListViewModel(repository = repository, logger = logger, app = application)
        viewModel.startLoading()
        delay(150)

        viewModel.handleIntent(ApplicationsListViewModel.Intent.ShowRunnableOnlyIntent(isRunnableOnly = false))

        val data = (viewModel.uiState.value as ApplicationsListViewModel.UiState.ScreenData)
        assertEquals(2, data.applications.size)
        assertEquals(false, data.runnableOnly)
    }

    @Test
    fun requestAppIcon_delegatesToRepository() = runBlocking {
        val bitmap = mockk<Bitmap>(relaxed = true)
        coEvery { repository.imageIcon(any(), "com.test", 64) } returns bitmap

        val viewModel = ApplicationsListViewModel(repository = repository, logger = logger, app = application)

        val result = viewModel.requestAppIcon("com.test", 64)

        assertEquals(bitmap, result)
    }
}
