package ru.vkdev.greentest.ui.list

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.vkdev.greentest.ui_common.dimen.DimensList
import ru.vkdev.greentest.ui_common.dimen.DimensScreen

private val iconSize = 42.dp
private val listItemHeight = 46.dp

@Composable
fun ApplicationsListScreen(paddingValues: PaddingValues) {
    ApplicationsListScreenContent(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    )
}

@Composable
internal fun ApplicationsListScreenContent(
    modifier: Modifier, viewModel: ApplicationsListViewModel = koinViewModel(key = "ApplicationsListScreen")
) {

    LaunchedEffect(viewModel) {
        viewModel.startLoading()
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val maxIconSize = with(LocalDensity.current) { iconSize.roundToPx() }

    when (state) {
        is ApplicationsListViewModel.UiState.Loading -> ApplicationsListLoading(modifier = modifier)
        is ApplicationsListViewModel.UiState.Error -> ApplicationsListError(modifier = modifier)
        is ApplicationsListViewModel.UiState.ScreenData -> ApplicationsListData(
            modifier = modifier,
            (state as ApplicationsListViewModel.UiState.ScreenData),
            onShowRunnableOnly = { onlyRunnable ->
                viewModel.handleIntent(ApplicationsListViewModel.Intent.ShowRunnableOnlyIntent(onlyRunnable))
            },
            requestAppIcon = { packageId ->
                viewModel.requestAppIcon(packageId = packageId, maxSize = maxIconSize)
            }
        )
    }
}

@Composable
internal fun ApplicationsListData(
    modifier: Modifier,
    data: ApplicationsListViewModel.UiState.ScreenData,
    requestAppIcon: suspend (String) -> Bitmap?,
    onShowRunnableOnly: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = DimensScreen.paddingHorizontal, vertical = DimensScreen.paddingVertical)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = data.runnableOnly, onCheckedChange = { checked ->
                onShowRunnableOnly(checked)
            })
            Spacer(Modifier.width(5.dp))
            Text(stringResource(R.string.runnable_only))
        }

        Spacer(Modifier.height(5.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = DimensList.contentPadding),
            verticalArrangement = Arrangement.spacedBy(DimensList.verticalSpacing)
        ) {
            items(
                items = data.applications, key = { it.packageId }) { app ->
                ListItem(item = app, requestDrawable = { packageId ->
                    requestAppIcon(packageId)
                })
            }
        }
    }
}

@Composable
internal fun ApplicationsListLoading(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ApplicationsListError(modifier: Modifier) {
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
internal fun ListItem(item: ApplicationsListViewModel.UiAppInfo, requestDrawable: suspend (String) -> Bitmap?) {
    Card(
        Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(DimensList.itemCardElevation)
    ) {
        Row(
            Modifier
                .heightIn(listItemHeight)
                .padding(vertical = DimensList.innerPaddingVertical, horizontal = DimensList.innerPaddingHorizontal)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {

            val bitmap by produceState<Bitmap?>(initialValue = null, item.packageId) {
                value = requestDrawable(item.packageId)
            }

            if (bitmap != null) {
                val imageBitmap = remember(item.packageId) { bitmap!!.asImageBitmap() }
                Image(
                    modifier = Modifier.size(iconSize),
                    bitmap = imageBitmap,
                    contentDescription = stringResource(R.string.content_desc_app_icon)
                )
            } else {
                Spacer(modifier = Modifier.size(iconSize))
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    text = item.appName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    item.packageId,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}