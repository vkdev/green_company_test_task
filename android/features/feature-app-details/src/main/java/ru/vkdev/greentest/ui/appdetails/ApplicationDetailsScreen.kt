package ru.vkdev.greentest.ui.appdetails

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.vkdev.greentest.ui.appdetails.model.UiAppDetails
import ru.vkdev.greentest.ui_common.dimen.DimensList
import ru.vkdev.greentest.ui_common.dimen.DimensScreen

private val iconSize = 42.dp

@Composable
fun ApplicationDetailsScreen(
    paddingValues: PaddingValues,
    packageId: String
) {
    ApplicationDetailsScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        packageId
    )
}

@Composable
internal fun ApplicationDetailsScreenContent(
    modifier: Modifier,

    packageId: String,
    viewModel: ApplicationDetailsViewModel = koinViewModel(parameters = { parametersOf(packageId) })
) {
    LaunchedEffect(viewModel) {
        viewModel.startLoading()
    }

    val state by viewModel.applicationDetails.collectAsStateWithLifecycle()
    val maxIconSize = with(LocalDensity.current) { iconSize.roundToPx() }

    when (state) {
        is ApplicationDetailsViewModel.UiState.Loading -> ApplicationDetailsLoading(modifier = modifier)
        is ApplicationDetailsViewModel.UiState.Error -> ApplicationDetailsError(modifier = modifier)
        is ApplicationDetailsViewModel.UiState.Success -> ApplicationDetailsData(
            modifier = modifier,
            details = (state as ApplicationDetailsViewModel.UiState.Success).details,
            requestDrawable = { packageId -> viewModel.requestAppIcon(packageId = packageId, maxSize = maxIconSize) },
            onOpenClick = {
                viewModel.handleIntent(
                    ApplicationDetailsViewModel.Intent.LaunchAppIntent(
                        (state as ApplicationDetailsViewModel.UiState.Success).details.packageId
                    )
                )
            }
        )
    }
}

@Composable
internal fun ApplicationDetailsLoading(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ApplicationDetailsError(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = DimensScreen.paddingHorizontal, vertical = DimensScreen.paddingVertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.screen__app_loading_error),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
internal fun ApplicationDetailsData(
    modifier: Modifier,
    details: UiAppDetails,
    requestDrawable: suspend (String) -> Bitmap?,
    onOpenClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = DimensScreen.paddingHorizontal, vertical = DimensScreen.paddingVertical),
        verticalArrangement = Arrangement.spacedBy(DimensList.verticalSpacing)
    ) {
        val bitmap by produceState<Bitmap?>(initialValue = null, details.packageId) {
            value = requestDrawable(details.packageId)
        }

        if (bitmap != null) {
            val imageBitmap = remember(details.packageId) { bitmap!!.asImageBitmap() }
            Image(
                modifier = Modifier.size(iconSize),
                bitmap = imageBitmap,
                contentDescription = stringResource(R.string.content_desc_app_icon)
            )
        }

        Text(details.appName)
        Text(stringResource(R.string.item__package_id, details.packageId))
        Text(stringResource(R.string.item__app_version, details.version))
        Text(stringResource(R.string.item__app_version_code, details.versionCode))

        details.hashSum?.let { hashSum ->
            Text(stringResource(R.string.item__app_hash_sum, hashSum))
        }

        if (details.hasLaunchedActivity) {
            Button(onClick = onOpenClick) {
                Text(stringResource(R.string.button__open_app))
            }
        }
    }
}