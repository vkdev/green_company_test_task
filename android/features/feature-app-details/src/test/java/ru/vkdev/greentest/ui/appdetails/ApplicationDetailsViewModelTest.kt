package ru.vkdev.greentest.ui.appdetails

import android.app.Application
import android.graphics.Bitmap
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.vkdev.greentest.logger.Logger
import ru.vkdev.greentest.repository_api.Repository
import ru.vkdev.greentest.repository_api.model.AppInfo
import ru.vkdev.greentest.repository_api.model.HashAlgorithm
import ru.vkdev.greentest.ui.appdetails.usecase.ApplicationLauncher

class ApplicationDetailsViewModelTest {

    private lateinit var repository: Repository
    private lateinit var applicationLauncher: ApplicationLauncher
    private lateinit var application: Application
    private lateinit var logger: Logger
    private val packageId = "com.test.app"

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        application = mockk(relaxed = true)
        applicationLauncher = mockk(relaxed = true)
        logger = mockk(relaxed = true)
    }

    @Test
    fun startLoading_repositoryReturnsSuccess_emitsUiAppDetails() = runBlocking {
        val appInfo = AppInfo(
            appName = "Test App",
            version = "1.0",
            packageId = packageId,
            versionCode = 1L,
            hasLaunchedActivity = true
        )
        coEvery { repository.installedAppBaseInfo(any(), packageId) } returns Result.success(appInfo)
        coEvery { repository.installedAppHash(any(), packageId, HashAlgorithm.SHA256) } returns
            Result.success(byteArrayOf(0xab.toByte(), 0xcd.toByte()))

        val viewModel = ApplicationDetailsViewModel(
            app = application,
            repository = repository,
            applicationLauncher = applicationLauncher,
            logger = logger,
            packageId = packageId
        )

        viewModel.startLoading()
        delay(150)

        val state = viewModel.applicationDetails.value
        assertTrue(state is ApplicationDetailsViewModel.UiState.UiAppDetails)
        val data = state as ApplicationDetailsViewModel.UiState.UiAppDetails
        assertEquals("Test App", data.appName)
        assertEquals(packageId, data.packageId)
        assertEquals("1.0", data.version)
        assertEquals(1L, data.versionCode)
        assertEquals(true, data.hasLaunchedActivity)
    }

    @Test
    fun startLoading_repositoryFails_emitsError() = runBlocking {
        coEvery { repository.installedAppBaseInfo(any(), packageId) } returns
            Result.failure(RuntimeException("Load failed"))

        val viewModel = ApplicationDetailsViewModel(
            app = application,
            repository = repository,
            applicationLauncher = applicationLauncher,
            logger = logger,
            packageId = packageId
        )

        viewModel.startLoading()
        delay(150)

        assertEquals(ApplicationDetailsViewModel.UiState.Error, viewModel.applicationDetails.value)
    }

    @Test
    fun startLoading_afterAppInfo_loadsHashAndUpdatesState() = runBlocking {
        val appInfo = AppInfo(
            appName = "App",
            version = "1.0",
            packageId = packageId,
            versionCode = 1L,
            hasLaunchedActivity = true
        )
        val hashBytes = "e3b0c44298fc1c14".toByteArray()
        coEvery { repository.installedAppBaseInfo(any(), packageId) } returns Result.success(appInfo)
        coEvery { repository.installedAppHash(any(), packageId, HashAlgorithm.SHA256) } returns
            Result.success(hashBytes)

        val viewModel = ApplicationDetailsViewModel(
            app = application,
            repository = repository,
            applicationLauncher = applicationLauncher,
            logger = logger,
            packageId = packageId
        )

        viewModel.startLoading()
        delay(200)

        val state = viewModel.applicationDetails.value
        assertTrue(state is ApplicationDetailsViewModel.UiState.UiAppDetails)
        val data = state as ApplicationDetailsViewModel.UiState.UiAppDetails
        assertTrue(data.hashSum != null)
        assertEquals(hashBytes.toHexString(), data.hashSum)
    }

    @Test
    fun handleIntent_LaunchAppIntent_callsApplicationLauncher() {
        every { applicationLauncher(packageId) } returns true

        val viewModel = ApplicationDetailsViewModel(
            app = application,
            repository = repository,
            applicationLauncher = applicationLauncher,
            logger = logger,
            packageId = packageId
        )

        viewModel.handleIntent(ApplicationDetailsViewModel.Intent.LaunchAppIntent(packageId))

        verify(exactly = 1) { applicationLauncher(packageId) }
    }

    @Test
    fun requestAppIcon_delegatesToRepository() = runBlocking {
        val bitmap = mockk<Bitmap>(relaxed = true)
        coEvery { repository.imageIcon(any(), packageId, 128) } returns bitmap

        val viewModel = ApplicationDetailsViewModel(
            app = application,
            repository = repository,
            applicationLauncher = applicationLauncher,
            logger = logger,
            packageId = packageId
        )

        val result = viewModel.requestAppIcon(packageId, 128)

        assertEquals(bitmap, result)
    }

    private fun ByteArray.toHexString(): String = joinToString("") { "%02x".format(it) }
}
